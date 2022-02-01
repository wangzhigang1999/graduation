package com.bupt.graduation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bupt.graduation.dao.PhotoDao;
import com.bupt.graduation.dao.PhotoUserRelationDao;
import com.bupt.graduation.entity.Photo;
import com.bupt.graduation.entity.PhotoUserRelation;
import com.bupt.graduation.entity.Resp;
import com.bupt.graduation.service.ImageSegService;
import com.bupt.graduation.service.UserService;
import com.bupt.graduation.utils.ImageUploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author wanz
 * @since 2020-05-09 13:16:33
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private final PhotoUserRelationDao relationDao;
    private final PhotoDao photoDao;

    final ImageSegService segService;

    private final RedisTemplate<String, String> redisTemplate;
    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 这是数据库中的状态标记,个人的状态
     * 用户确认信息后,将其状态更改为 3
     * 更改要与数据库同时更改
     */
    private final static Integer CONFIRMED = 3;

    /**
     * 这是返回给前端同学的标记字段
     * 表示角色为管理员,值更改前与前端同学联系
     */
    private final static String ADMINISTRATOR = "Administrator";

    /**
     * 这是返回给前端同学的标记字段
     * 表示角色为加入者,值更改前与前端同学联系
     */
    private final static String JOINED = "Joined";

    /**
     * 最终的合影,配合redis使用
     */
    final static String FINAL_IMG = "_FINAL_IMAGE";


    /**
     * 下边这几个状态均为 Redis 中存储的信息的后缀，前缀应该是 uuid
     * 同时所有的图片名称都直接存储在了以uuid为键的set中，便于日后删除
     */
    final static String CONFIRMED_CNT = "_CONFIRMED_CNT";
    final static String JOINED_CNT = "_JOINED_CNT";

    final static String CONFIRMED_SET = "_CONFIRMED_SET";
    final static String UPLOADED_SET = "_UPLOADED_SET";
    final static String JOINED_SET = "_JOINED_SET";

    public UserServiceImpl(PhotoUserRelationDao relationDao, PhotoDao photoDao, RedisTemplate<String, String> redisTemplate, @Qualifier("modnet") ImageSegService segService) {
        this.relationDao = relationDao;
        this.photoDao = photoDao;
        this.redisTemplate = redisTemplate;
        this.segService = segService;
    }

    /**
     * 返回前用户参加的和管理的所有的合照信息
     *
     * @param openId 某个用户的openId
     * @return res
     */
    @Override
    public Object getMyPhotoInfo(String openId) {

        // 从数据库中查一下这个人的数据
        List<Photo> photos = relationDao.getUserPhotosByOpenId(openId);

        // 为null ,必然发生了错误
        if (photos == null) {
            return new Resp(false, 200, "Current user's information does not exist", null);
        } else {
            HashMap<Object, Object> adminAndUser = new HashMap<>(2);

            List<HashMap<String, Object>> admin = new LinkedList<>();
            List<HashMap<String, Object>> user = new LinkedList<>();

            // 对当前用户名下的所有合照进行分析统计
            processCurrentUser(openId, photos, admin, user);

            adminAndUser.put(ADMINISTRATOR, admin);
            adminAndUser.put(JOINED, user);

            return new Resp("success", adminAndUser);
        }

    }

    /**
     * 对当前用户名下的所有合照进行统计分类,对每一张合照判断当前用户的状态和角色
     *
     * @param openId 当前用户的openId
     * @param photos 数据库中查出来的所有的数据
     * @param admin  存储管理员的 HashMap
     * @param joined 存储加入的HashMap
     */
    private void processCurrentUser(String openId, List<Photo> photos, List<HashMap<String, Object>> admin, List<HashMap<String, Object>> joined) {
        photos.parallelStream().forEach(photo -> {

            photo.setBackgroundImg(photo.getBackgroundImg());
            photo.setFinalImg(photo.getFinalImg());

            // 获取当前合照加入的人数
            String confirm = redisTemplate.opsForValue().get(photo.getPhotoId() + CONFIRMED_CNT);

            //获取当前合照确认的人数
            String joiner = redisTemplate.opsForValue().get(photo.getPhotoId() + JOINED_CNT);

            if (confirm == null) {
                confirm = "0";
            }
            if (joiner == null) {
                joiner = "0";
            }

            HashMap<String, Object> hashMap = new HashMap<>();


            // 判断每个人的状态,如果当前合照已经发布了,那么久不需要判断每个人的状态
            if (photo.getStatus().equals(AdminServiceImpl.RELEASED)) {
                hashMap.put("userStatus", "可以领取合照了");

                //或者当前所有人已经确认,正在等待管理员进行发布
            } else if (photo.getStatus().equals(AdminServiceImpl.CONFIRMED_FOR_RELEASE)) {
                hashMap.put("userStatus", "已确认待发布");
            } else {
                // 万不得已判断每个人的状态
                Boolean isConfirmed = redisTemplate.opsForSet().isMember(photo.getPhotoId() + CONFIRMED_SET, openId);
                if (isConfirmed != null && isConfirmed.equals(true)) {
                    hashMap.put("userStatus", "已确认待发布");
                } else {
                    Boolean isUploaded = redisTemplate.opsForSet().isMember(photo.getPhotoId() + UPLOADED_SET, openId);
                    if (isUploaded != null && isUploaded.equals(true)) {
                        hashMap.put("userStatus", "已上传待确认");
                    } else {
                        hashMap.put("userStatus", "待上传");
                    }
                }

            }


            hashMap.put("confirmed", confirm);
            hashMap.put("joined", joiner);
            hashMap.put("photo", photo);

            if (photo.getCreatorOpenId().equals(openId)) {
                admin.add(hashMap);
            }
            joined.add(hashMap);

        });
    }

    @Override
    public Object confirmUpload(String uuid, String openId) {


        // 先检查一下用户是否已经确认了,已经确认过则不进行操作
        Boolean isMember = redisTemplate.opsForSet().isMember(uuid + CONFIRMED_SET, openId);
        assert isMember != null;
        if (isMember) {
            return new Resp("success", null);
        }

        // 判断是否一应有了必备的信息
        String relation = relationDao.getFinalImage(uuid, openId);
        if (relation == null) {
            logger.error("在上传之前确认，非法操作！ uuid={} openId={}", uuid, openId);
            return new Resp(false, 200, "还不能进行确认，因为未上传最终的照片！", null);
        }
        // 修改数据库,返回修改成功的数据条数
        Integer confirmStatus = relationDao.confirmStatus(uuid, openId);

        if (confirmStatus == 1) {
            // 在 redis 中将对应的值加一
            Long confirmed = redisTemplate.opsForValue().increment(uuid + CONFIRMED_CNT);

            String total = redisTemplate.opsForValue().get(uuid + AdminServiceImpl.TOTAL_NUMBER_OF_PEOPLE);


            // 判断一下是不是所有人都加入了,是的话就更新一下数据库中的状态
            assert confirmed != null;
            assert total != null;
            if (confirmed.equals(Long.parseLong(total))) {
                photoDao.changeStatus(uuid, AdminServiceImpl.CONFIRMED_FOR_RELEASE);
            }
            // 把这个用户添加到已确认的集合中去
            redisTemplate.opsForSet().add(uuid + CONFIRMED_SET, openId);
            return new Resp("success", null);
        } else {
            return new Resp(false, 200, "未知错误，请重试", null);
        }

    }

    @Override
    public Object changeBasicInfo(String openId, String name, String uuid, String remarks) {

        // 修改之前先查询一下当前的状态，如果是已确认，则不允许修改
        if (checkStatus(uuid, openId)) {
            return new Resp(false, 200, "确认后的信息不能修改！", null);
        }

        return setInfo(openId, name, uuid, remarks);

    }

    private Object setInfo(String openId, String name, String uuid, String remarks) {
        int res = getRes(openId, name, uuid, remarks);

        if (res == 1) {
            return new Resp("success", null);
        } else {
            return new Resp(false, 200, "未知错误！", null);
        }
    }

    private int getRes(String openId, String name, String uuid, String remarks) {
        return this.relationDao.addBasicInfo(openId, uuid, name, remarks);
    }

    @Override
    public Object addBasicInfo(String uuid, String openId, String remarks, String name) {
        return setInfo(openId, name, uuid, remarks);
    }

    /**
     * 这个方法与更新图片是公用的,因此要先判断状态
     */
    @Override
    public Object uploadImage(String uuid, String openId, MultipartFile file) {


        // 修改之前先查询一下当前的状态，如果是已确认，则不允许修改
        if (checkStatus(uuid, openId)) {
            return new Resp(false, 200, "已经确认的图片不能再修改！", null);
        }

        String imgUrl;

        imgUrl = segService.seg(file);
        // 提取失败
        if (imgUrl == null) {
            return new Resp(false, 200, "matting 失败！ 请稍后重试", null);
        }

        // 用户信息上传,将用户本地的图片和SEG人像同时存储到数据库中
        Integer res = relationDao.uploadImage(uuid, openId, "", imgUrl);

        if (res == 1) {
            HashMap<String, String> map = new HashMap<>(1);
            map.put("url", imgUrl);
            return new Resp("success", map);
        } else {
            logger.error("写数据库失败！ uuid={} url={}", uuid, imgUrl);
            return new Resp(false, 200, "Failed to write the image address to the database", null);
        }
    }


    @Override
    public Object changeImage(String uuid, String openId, MultipartFile file) {
        return uploadImage(uuid, openId, file);
    }


    @Override
    public Object getMyPhotoInfoBeforeChangeIt(String uuid, String openId) {
        QueryWrapper<PhotoUserRelation> wrapper = new QueryWrapper<>();
        wrapper.eq("photo_id", uuid).eq("user_open_id", openId);
        PhotoUserRelation relation = relationDao.selectOne(wrapper);
        if (relation == null) {
            return new Resp(false, 200, "Group photo does not exist", null);
        }

        relation.setUserFinalImg(relation.getUserFinalImg());
        relation.setUserTempImg(relation.getUserTempImg());
        relation.setUserUploadedImg(relation.getUserUploadedImg());


        return new Resp(true, 200, "success", relation);

    }

    @Override
    public Object isJoined(String uuid, String openId) {
        Boolean isMember = redisTemplate.opsForSet().isMember(uuid + JOINED_SET, openId);
        return new Resp(true, 200, "success", isMember);
    }

    @Override
    public Object uploadFinalImage(String uuid, String openId, MultipartFile file) {
        // 检查一下状态,如果已经确认,则不允许进行上传
        if (checkStatus(uuid, openId)) {
            return new Resp(false, 200, "Currently users are not allowed to modify personal information", null);
        }


        String finalUrl;
        try {
            finalUrl = ImageUploadUtil.saveOnline(file);
        } catch (IOException e) {
            return new Resp(false, 200, "There was a problem while uploading", null);
        }

        if (finalUrl == null) {
            return new Resp(false, 200, "There was a problem while uploading", null);
        }

        Integer res = relationDao.uploadFinalImage(uuid, openId, finalUrl);

        // 在集合中添加数据,注意如果是修改的话返回值会为0
        redisTemplate.opsForSet().add(uuid + UPLOADED_SET, openId);

        if (res == 1) {
            return new Resp("success", null);
        } else {
            return new Resp(false, 200, "There was a problem while uploading", null);
        }

    }

    /**
     * 检查一条记录是否允许修改,如果不可以修改,返回false
     *
     * @param uuid   uuid
     * @param openId openId
     * @return boolean
     */
    private boolean checkStatus(String uuid, String openId) {

        Integer status = relationDao.getStatusByUuidAndOpenId(uuid, openId);

        return CONFIRMED.equals(status);
    }


    @Override
    public Object getGroupPhoto(String uuid) {

        String photoLink = redisTemplate.opsForValue().get(uuid + FINAL_IMG);
        if (photoLink == null) {
            synchronized (this) {
                photoLink = photoDao.getFinalPhotoLink(uuid);
                if (photoLink == null) {
                    return new Resp(false, 200, "making ...", null);
                } else {
                    redisTemplate.opsForValue().set(uuid + FINAL_IMG, photoLink);
                    return new Resp(true, 200, "success", photoLink);
                }

            }

        } else {
            return new Resp(true, 200, "success", photoLink);
        }

    }

    @Override
    public Object getPhotoInfoByInvitationCode(String uuid) {

        Photo photo = relationDao.getPhotoInfoByInvitationCode(uuid);

        if (photo == null) {
            return new Resp(false, 200, "Query photo does not exist", null);
        }

        photo.setBackgroundImg(photo.getBackgroundImg());


        String confirmed = redisTemplate.opsForValue().get(uuid + CONFIRMED_CNT);
        String joined = redisTemplate.opsForValue().get(uuid + JOINED_CNT);

        if (confirmed == null) {
            confirmed = "0";
        }
        if (joined == null) {
            joined = "0";
        }

        HashMap<String, Object> map = new HashMap<>(3);
        map.put("confirmed", confirmed);
        map.put("joined", joined);
        map.put("photo", photo);

        return new Resp(true, 200, "success", map);
    }

    @Override
    public Resp joinSomeOne(String openId, String uuid) {


        Boolean isMember = redisTemplate.opsForSet().isMember(uuid + JOINED_SET, openId);

        assert (isMember != null);
        if (isMember) {
            return new Resp(false, 200, "Already joined", null);
        }

        // 在数据库中添加相应的数据
        int res = this.relationDao.addRelation(openId, uuid);

        if (res == 1) {

            // 将redis的值自增,表示当前加入合照的人数加一
            Long increment = redisTemplate.opsForValue().increment(uuid + JOINED_CNT);

            // 将当前的用户加入到 “已加入” 的集合中
            redisTemplate.opsForSet().add(uuid + JOINED_SET, openId);

            if (increment != null) {
                return new Resp("success", null);
            } else {
                return new Resp(false, 200, "", null);
            }
        } else {
            return new Resp(false, 200, "", null);
        }
    }
}


