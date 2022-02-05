package com.bupt.graduation.service.impl;

import com.bupt.graduation.aspect.LoggerAspect;
import com.bupt.graduation.dao.PhotoDao;
import com.bupt.graduation.dao.PhotoUserRelationDao;
import com.bupt.graduation.entity.Pair;
import com.bupt.graduation.entity.Resp;
import com.bupt.graduation.service.AdminService;
import com.bupt.graduation.service.ImageUploadService;
import com.bupt.graduation.service.UserService;
import com.bupt.graduation.utils.Config;
import com.bupt.graduation.utils.ImageConcatUtil;
import com.bupt.graduation.utils.MultiThreadDownloadUtil;
import com.bupt.graduation.utils.SimpleDownLoadUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * @author wangz
 * @since 2020-05-09 13:15:35
 */
@Service("photoService")
public class AdminServiceImpl implements AdminService {

    final PhotoDao photoDao;
    final PhotoUserRelationDao relationDao;
    final UserService userService;
    final ImageUploadService uploadService;

    final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);


    /**
     * 连接redis
     */
    final RedisTemplate<String, String> redisTemplate;

    /**
     * 默认的图片目录
     */
    final static String BASE_DIR = Config.BASE_DIR;

    /**
     * 已确认待发布.
     */
    final static int CONFIRMED_FOR_RELEASE = 2;

    /**
     * 已发布
     */
    final static int RELEASED = 3;

    /**
     * 没什么意义的全局常量
     */
    final static String TOTAL_NUMBER_OF_PEOPLE = "_TOTAL_CNT";


    public AdminServiceImpl(PhotoUserRelationDao relationDao, PhotoDao photoDao, UserService userService, RedisTemplate<String, String> redisTemplate, @Qualifier("remote") ImageUploadService uploadService) {
        this.relationDao = relationDao;
        this.photoDao = photoDao;
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.uploadService = uploadService;
    }

    /**
     * @param uuid          uuid
     * @param imgName       合照的名称
     * @param schoolName    学校名称
     * @param className     班级名称
     * @param studentNumber 学生数量
     * @param otherInfo     其他的一些信息
     * @param openId        openId
     * @return res
     */
    @Override
    public Object createBasicInfo(String uuid, String imgName, String schoolName, String className, String studentNumber, String otherInfo, String openId) {

        Integer count = photoDao.getCount(uuid);

        if (count >= 1) {
            logger.warn("尝试添加已经存在的合照 uuid={} openid={}", uuid, openId);
            return new Resp(false, 200, "already exists", null);
        }

        Integer res = photoDao.createBasicInfo(uuid, imgName, schoolName, className, Integer.parseInt(studentNumber), otherInfo, openId);

        if (res == 1) {
            // 关联管理员与 photo
            Resp o = (Resp) userService.joinSomeOne(openId, uuid);
            if (!o.isSuccess()) {
                return new Resp(false, 200, "please try again", null);
            }

            // 在redis中缓存合照的所有人数,以便将来判断是否所有人都加入完成了
            redisTemplate.opsForValue().set(uuid + TOTAL_NUMBER_OF_PEOPLE, studentNumber);
            return new Resp(true, 200, "success", null);
        } else {
            logger.warn("创建合照失败！ uuid={} openId={}", uuid, openId);
            return new Resp(false, 200, "please try again", null);
        }

    }

    /**
     * @param uuid       uuid
     * @param imgName    合照的名称
     * @param schoolName 学校名称
     * @param className  班级名称
     * @param otherInfo  其他的一些信息
     * @return res
     */
    @Override
    public Object changeBasicInfo(String uuid, String imgName, String schoolName, String className, String otherInfo) {


        Integer res = photoDao.changeBasicInfo(uuid, imgName, schoolName, className, otherInfo);

        if (res == 1) {
            return new Resp(true, 200, "success", null);
        } else {
            logger.warn("尝试改变基础信息失败！ uuid={} imgName={}  schoolName={}  className={} otherInfo={}", uuid, imgName, schoolName, className, otherInfo);
            return new Resp(false, 200, "failed", null);
        }

    }

    @Override
    public Object getOrder(String uuid) {

        List<HashMap<String, String>> positions = relationDao.getOrders(uuid);

        if (!positions.isEmpty()) {
            if ((positions.get(0).get("relative_position") != null)) {
                return new Resp(true, 200, "success", positions);
            }
        }

        int index = 0;
        for (HashMap<String, String> map : positions) {
            map.put("relative_position", String.valueOf(index++));
        }

        return new Resp(true, 200, "success", positions);
    }

    @Override
    public Object changePeopleOrder(String uuid, String orders) {


        String[] finalOrder = new Gson().fromJson(orders, String[].class);

        if (finalOrder == null) {
            logger.warn("调整顺序失败！ uuid={} order={}", uuid, orders);
            return new Resp(false, 200, "parameter cannot be empty", null);
        }


        if (finalOrder.length > 0) {
            for (int i = 0; i < finalOrder.length; i++) {
                changePeopleOrder(uuid, finalOrder[i], i);
            }
        }

        return new Resp(true, 200, "success", null);
    }


    @Override
    public Object uploadBackgroundImg(String uuid, MultipartFile file) {
        return uploadService.upload(uuid, file);
    }


    @Override
    public Object changeBackgroundImg(String uuid, MultipartFile file) {
        return uploadBackgroundImg(uuid, file);
    }

    public void changePeopleOrder(String uuid, String openId, int position) {
        relationDao.changePeopleOrder(uuid, openId, position);
    }

    /**
     * 开始生成人像
     *
     * @param uuid uuid uuid
     * @return res
     */
    @Override
    public Object generate(String uuid) {
        if (photoDao.getStatus(uuid) == RELEASED) {
            logger.warn("尝试再次生成合照！ uuid={}", uuid);
            return new Resp(false, 200, "合照已生成！", null);
        }
        // 获取所有的人像的链接
        List<Pair> list = relationDao.getImgAndPositionByUuid(uuid);
        // 对这些人进行排序
        assert list != null;
        for (Pair pair : list) {
            if (pair == null || pair.getKey() == null || pair.getValue() == null) {
                return new Resp(false, 200, "请等待所有人确认完毕再合成！", null);
            }
        }

        list.sort(Comparator.comparingInt(Pair::getValue));

        // 将排好顺序的人的图片链接写入一个数组中
        String[] ossImageLink = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ossImageLink[i] = list.get(i).getKey();
        }

        String[] localImage = new String[list.size()];

        // 首先使用多线程下载，如果多线程下载失败，用单线程兜底
        try {
            localImage = MultiThreadDownloadUtil.download(ossImageLink);
        } catch (InterruptedException | ExecutionException e) {

            for (int i = 0; i < ossImageLink.length; i++) {
                UUID uid = UUID.randomUUID();
                String savePath = BASE_DIR + uid + ".jpg";
                localImage[i] = (savePath);
                SimpleDownLoadUtil.download(savePath, ossImageLink[i]);
            }
        }

        String imageLink = ImageConcatUtil.concat(localImage);

        if (imageLink == null) {
            logger.error("合成图片失败！ uuid={}", uuid);
            return new Resp(false, 200, "合成失败，请稍后重试！", null);
        } else {
            //把生成的人像存进去
            photoDao.releaseConfirm(uuid, CONFIRMED_FOR_RELEASE, imageLink);
            return new Resp(true, 200, "success", imageLink);
        }


    }

    @Override
    public Object isConfirmComplete(String uuid) {
        try {
            String res = redisTemplate.opsForValue().get(uuid + UserServiceImpl.CONFIRMED_CNT);
            return res == null ? new Resp(true, 200, "success", "0") : new Resp(true, 200, "success", res);
        } catch (Exception e) {
            return new Resp(false, 200, "Failed to get the number of people joined!", null);
        }
    }

    /**
     * 由管理员发布最终的合影
     *
     * @param uuid uuid
     * @return res
     */
    @Override
    public Object releaseConfirm(String uuid) {

        String link = photoDao.getFinalPhotoLink(uuid);
        if (link == null || link.length() < 1) {
            return new Resp(false, 200, "Please generate before publish it", null);
        }
        // 在redis中加入
        redisTemplate.opsForValue().set(uuid + UserServiceImpl.FINAL_IMG, link);
        // 完成状态的确认
        Integer integer = photoDao.changeStatus(uuid, RELEASED);

        if (integer != 1) {
            return new Resp(false, 200, "An unknown error has occurred", null);
        }
        // 返回信息
        return new Resp(true, 200, "success", null);

    }


}

