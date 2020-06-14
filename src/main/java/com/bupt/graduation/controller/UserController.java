package com.bupt.graduation.controller;

import com.bupt.graduation.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * (User)表控制层
 *
 * @author wanz
 * @since 2020-05-09 13:16:33
 */
@RestController
@RequestMapping("/graduation/user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;


    @RequestMapping("/join")
    public Object joinSomeOne(String openId, String uuid) {
        return userService.joinSomeOne(openId, uuid);
    }

    @RequestMapping("/addBasicInfo")
    public Object joinSomeOne(String openId, String uuid, String remark, String userName) {
        return userService.addBasicInfo(uuid, openId, remark, userName);
    }


    @RequestMapping("/uploadImage")
    public Object uploadImage(String openId, String uuid, MultipartFile file) {
        return userService.uploadImage(uuid, openId, file);
    }

    @RequestMapping("/uploadFinalImage")
    public Object uploadFinalImage(String uuid, String openId, MultipartFile file) {
        return userService.uploadFinalImage(uuid, openId, file);
    }

    @RequestMapping("/confirmUpload")
    public Object confirm(String uuid, String openId) {
        return userService.confirmUpload(uuid, openId);
    }

    @RequestMapping("/changeBasicInfo")
    public Object changeBasicInfo(String openId, String userName, String uuid, String remark) {
        return userService.changeBasicInfo(openId, userName, uuid, remark);
    }


    @RequestMapping("/changeImage")
    public Object changeImage(String uuid, String openId, MultipartFile file) {
        return userService.changeImage(uuid, openId, file);
    }


    @RequestMapping("/getGroupPhoto")
    public Object getGroupPhoto(String uuid) {
        return userService.getGroupPhoto(uuid);
    }


    @RequestMapping("/my")
    public Object my(String openId) {
        return userService.getMyPhotoInfo(openId);
    }

    /**
     * 注意 这个接口微信小程序使用 post 无法传值
     * @param uuid uuid
     * @return res
     */
    @RequestMapping("/getPhotoInfoByInvitationCode")
    public Object getPhotoInfoByInvitationCode(String uuid) {
        return userService.getPhotoInfoByInvitationCode(uuid);
    }

    @RequestMapping("/getMyPhotoInfoBeforeChangeIt")
    public Object getMyPhotoInfoBeforeChangeIt(String uuid,String openId){
        return userService.getMyPhotoInfoBeforeChangeIt(uuid, openId);
    }

}