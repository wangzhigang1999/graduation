package com.bupt.graduation.service;

import com.bupt.graduation.annotation.ExistCheck;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author wangz
 * @since 2020-05-09 13:16:33
 */
public interface UserService {

    /**
     * 返回前用户参加的和管理的所有的合照信息
     *
     * @param openId 某个用户的openId
     * @return 当前用户参加的和管理的所有的合照信息
     */
    Object getMyPhotoInfo(String openId);


    /**
     * 用户确认上传的信息
     *
     * @param uuid   uuid
     * @param openId openId
     * @return res
     */
    Object confirmUpload(String uuid, String openId);


    /**
     * 用户更新自己的个人信息
     *
     * @param openId openId
     * @param name   name
     * @param uuid   uuid
     * @param remark remark
     * @return res
     */
    Object changeBasicInfo(String openId, String name, String uuid, String remark);


    /**
     * 完善部分信息,此时关系库中应该已经存在一条信息
     *
     * @param uuid    uuid
     * @param openId  openId
     * @param remarks remark
     * @param name    name
     * @return res
     */
    Object addBasicInfo(String uuid, String openId, String remarks, String name);


    /**
     * 用户上传自己的个人照片
     *
     * @param uuid   uuid
     * @param openId openId
     * @param file   file
     * @return res
     */
    Object uploadImage(String uuid, String openId, MultipartFile file);

    /**
     * 用户上传最终修改完毕的的个人照片
     *
     * @param uuid   uuid
     * @param openId openId
     * @param file   file
     * @return res
     */
    Object uploadFinalImage(String uuid, String openId, MultipartFile file);

    /**
     * 用户更改自己的个人照片
     *
     * @param uuid   uuid
     * @param openId openId
     * @param file   file
     * @return res
     */
    Object changeImage(String uuid, String openId, MultipartFile file);

    /**
     * 用户加入一个合照
     *
     * @param openId openId
     * @param uuid   uuid
     * @return res
     */
    Object joinSomeOne(String openId, String uuid);


    /**
     * 领取合照
     *
     * @param uuid uuid
     * @return res
     */
    Object getGroupPhoto(String uuid);


    /**
     * 根据邀请码查询对应的合照信息
     *
     * @param uuid uuid
     * @return res
     */
    Object getPhotoInfoByInvitationCode(String uuid);


    /**
     * 在修改之前获取信息
     *
     * @param uuid   uuid
     * @param openId openId
     * @return res
     */
    Object getMyPhotoInfoBeforeChangeIt(String uuid, String openId);


    /**
     * 判断一个人是否已经加入了合照
     *
     * @param uuid   uuid
     * @param openId openid
     * @return res
     */
    Object isJoined(String uuid, String openId);

}