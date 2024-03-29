package com.bupt.graduation.controller;

import com.bupt.graduation.service.AdminService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wanz
 * @since 2020-05-09 13:15:37
 */
@RestController
@RequestMapping("/graduation/admin")
public class AdminController {


    final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping("/createBasicInfo")
    public Object createBasicInfo(String uuid, String imgName, String schoolName, String className, String studentNumber, String otherInfo, String openId) {


        return adminService.createBasicInfo(uuid, imgName, schoolName, className, studentNumber, otherInfo, openId);
    }


    @RequestMapping("/changeBasicInfo")
    public Object changeBasicInfo(String uuid, String imgName, String schoolName, String className, String otherInfo) {
        return adminService.changeBasicInfo(uuid, imgName, schoolName, className, otherInfo);
    }


    @RequestMapping("/uploadBackgroundImg")
    public Object uploadBackgroundImg(String uuid, MultipartFile file) {
        return adminService.uploadBackgroundImg(uuid, file);
    }


    @RequestMapping("/changeBackgroundImg")
    public Object changeBackgroundImg(String uuid, MultipartFile file) {
        return adminService.changeBackgroundImg(uuid, file);
    }


    @RequestMapping("/changePeopleOrder")
    public Object changePeopleOrder(String uuid, String openId) {
        return adminService.changePeopleOrder(uuid, openId);
    }


    @RequestMapping("/generate")
    public Object generate(String uuid) {
        return adminService.generate(uuid);
    }


    @RequestMapping("/isConfirmComplete")
    public Object isConfirmComplete(String uuid) {
        return adminService.isConfirmComplete(uuid);
    }

    @RequestMapping("/getOrder")
    public Object getOrderBeforeChangeIt(String uuid) {
        return adminService.getOrder(uuid);
    }

    @RequestMapping("/releaseConfirm")
    public Object releaseConfirm(String uuid) {
        return adminService.releaseConfirm(uuid);
    }
}

