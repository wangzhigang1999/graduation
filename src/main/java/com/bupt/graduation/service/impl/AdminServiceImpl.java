package com.bupt.graduation.service.impl;

import com.bupt.graduation.dao.PhotoDao;
import com.bupt.graduation.dao.PhotoUserRelationDao;
import com.bupt.graduation.entity.Pair;
import com.bupt.graduation.entity.Resp;
import com.bupt.graduation.service.AdminService;
import com.bupt.graduation.service.ImageUploadService;
import com.bupt.graduation.service.UserService;
import com.bupt.graduation.utils.CommonQueryUtil;
import com.bupt.graduation.utils.ImageCompositingUtil;
import com.bupt.graduation.utils.ImageDeleteUtil;
import com.bupt.graduation.utils.ImageDownLoadUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author wangz
 * @since 2020-05-09 13:15:35
 */
@Service("photoService")
public class AdminServiceImpl implements AdminService {

    final PhotoDao photoDao;
    final PhotoUserRelationDao relationDao;
    final UserService userService;
    @Qualifier("remote")
    @Autowired
    ImageUploadService uploadService;

    @Autowired
    CommonQueryUtil commonQueryUtil;


    /**
     * 连接redis
     */
    final RedisTemplate<String, String> redisTemplate;

    /**
     * 默认的图片目录
     */
    final static String BASE_DIR = System.getProperties().getProperty("user.dir") + "/IMG/";

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


    public AdminServiceImpl(PhotoUserRelationDao relationDao, PhotoDao photoDao, UserService userService, RedisTemplate<String, String> redisTemplate) {
        this.relationDao = relationDao;
        this.photoDao = photoDao;
        this.userService = userService;
        this.redisTemplate = redisTemplate;
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


        try {
            Integer res = photoDao.createBasicInfo(uuid, imgName, schoolName, className, Integer.parseInt(studentNumber), otherInfo, openId);
            if (res == 1) {
                // 关联管理员与 photo
                Resp o = (Resp) userService.joinSomeOne(openId, uuid);
                if (!o.isSuccess()) {
                    throw new Exception("");
                }

                // 在redis中缓存合照的所有人数,以便将来判断是否所有人都加入完成了
                redisTemplate.opsForValue().set(uuid + TOTAL_NUMBER_OF_PEOPLE, studentNumber);

                return new Resp(true, 200, "success", null);
            } else {
                throw new Exception("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Resp(false, 200, "Creation failed", null);
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

        try {
            // 判断一下是否存在
            if (commonQueryUtil.notExist(uuid)) {
                return new Resp(false, 200, "Group photo does not exist", null);
            }
            // 开始修改
            Integer res = photoDao.changeBasicInfo(uuid, imgName, schoolName, className, otherInfo);

            if (res == 1) {
                return new Resp(true, 200, "success", null);
            } else {
                throw new Exception("");
            }
        } catch (Exception e) {
            return new Resp(false, 200, "Failed to change information", null);
        }

    }

    @Override
    public Object getOrder(String uuid) {

        List<HashMap<String, String>> relations = relationDao.getOrders(uuid);

        if (!relations.isEmpty()) {
            if ((relations.get(0).get("relative_position") != null)) {
                return new Resp(true, 200, "success", relations);
            }
        }

        int index = 0;
        for (HashMap<String, String> map : relations) {
            map.put("relative_position", String.valueOf(index++));
        }

        return new Resp(true, 200, "success", relations);
    }

    @Override
    public Object changePeopleOrder(String uuid, String orders) {


        String[] finalOrder = new Gson().fromJson(orders, String[].class);

        if (finalOrder == null) {
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
            return new Resp(false, 200, "Currently unavailable", null);

        }
        // 获取所有的人像的链接
        List<Pair> list = relationDao.getImgAndPositionByUuid(uuid);
        // 对这些人进行排序
        assert list != null;

        for (Pair pair : list) {
            if (pair == null || pair.getKey() == null || pair.getValue() == null) {
                return new Resp(false, 200, "Please wait for everyone to join and sort", null);
            }
        }


        list.sort(Comparator.comparingInt(Pair::getValue));


        // 将排好顺序的人的图片链接写入一个数组中
        String[] img = new String[list.size()];

        //数组的索引
        AtomicInteger index = new AtomicInteger();

        list.forEach(o -> {
            UUID uid = UUID.randomUUID();
            String savePath = BASE_DIR + uid + ".jpg";
            ImageDownLoadUtil.downImages(savePath, o.getKey());
            img[index.getAndIncrement()] = (savePath);
        });
        // 开始拼接
        try {

            String imageLink = ImageCompositingUtil.overlapImage(img);

            if (imageLink == null) {
                throw new NullPointerException();
            } else {

                //把生成的人像存进去
                photoDao.releaseConfirm(uuid, CONFIRMED_FOR_RELEASE, imageLink);


                return new Resp(true, 200, "success", imageLink);
            }

        } catch (Exception e) {
            return new Resp(false, 200, "An error occurred while compositing the photo!", null);
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
        // 开始删除不使用的照片
        ImageDeleteUtil.delete(redisTemplate.opsForSet().members(uuid));


        if (integer != 1) {
            return new Resp(false, 200, "An unknown error has occurred", null);

        }
        // 返回信息
        return new Resp(true, 200, "success", null);

    }


}

