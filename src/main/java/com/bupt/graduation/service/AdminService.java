package com.bupt.graduation.service;

import com.bupt.graduation.annotation.ExistCheck;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * (Photo)表服务接口
 *
 * @author wangz
 * @since 2020-05-09 13:15:35
 */

public interface AdminService {

    /**
     * 用于增加一张合照
     *
     * @param openId        openId
     * @param uuid          uuid
     * @param imgName       合照的名称
     * @param schoolName    学校名称
     * @param className     班级名称
     * @param studentNumber 学生数量
     * @param otherInfo     其他的一些信息
     * @return res
     */
    Object createBasicInfo(String uuid, String imgName, String schoolName, String className,
                           String studentNumber, String otherInfo, String openId);

    /**
     * 用于更改合照的一些信息
     *
     * @param uuid       uuid
     * @param imgName    合照的名称
     * @param schoolName 学校名称
     * @param className  班级名称
     * @param otherInfo  其他的一些信息
     * @return res
     */
    @ExistCheck
    Object changeBasicInfo(String uuid, String imgName, String schoolName, String className, String otherInfo);


    /**
     * 在修改人员之前获得人员的顺序
     *
     * @param uuid uuid
     * @return res
     */
    @ExistCheck
    Object getOrder(String uuid);

    /**
     * 上传合照的背景
     *
     * @param file file
     * @param uuid uuid
     * @return res
     */
    @ExistCheck
    Object uploadBackgroundImg(String uuid, MultipartFile file);


    /**
     * 更改合照的背景
     *
     * @param file file
     * @param uuid uuid
     * @return res
     */
    @ExistCheck
    Object changeBackgroundImg(String uuid, MultipartFile file);


    /**
     * 由管理员手动更改用户的相对位置
     *
     * @param uuid     uuid
     * @param openId   openId
     * @param position position
     */
    @ExistCheck
    void changePeopleOrder(String uuid, String openId, int position);


    /**
     * 更改所有用户的顺序
     *
     * @param uuid   uuid
     * @param orders 所有用户的顺序的数组
     * @return res
     */
    @ExistCheck
    Object changePeopleOrder(String uuid, String orders);

    /**
     * 生成合照
     *
     * @param uuid uuid
     * @return res
     */
    @ExistCheck
    Object generate(String uuid);

    /**
     * 判断是否所有人都确认完成
     *
     * @param uuid uuid
     * @return res
     */
    @ExistCheck
    Object isConfirmComplete(String uuid);

    /**
     * 确认发布
     *
     * @param uuid uuid
     * @return res
     */
    @ExistCheck
    Object releaseConfirm(String uuid);
}