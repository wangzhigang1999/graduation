package com.bupt.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * (PhotoUserRelation)实体类
 *
 * @author wangz
 * @since 2020-05-09 13:15:31
 */
public class PhotoUserRelation implements Serializable {
    private static final long serialVersionUID = 643709644255110383L;

    @Override
    public String toString() {
        return "PhotoUserRelation{" +
                "id=" + id +
                ", photoId='" + photoId + '\'' +
                ", userOpenId='" + userOpenId + '\'' +
                ", createTime=" + createTime +
                ", userUploadedImg='" + userUploadedImg + '\'' +
                ", userRemark='" + userRemark + '\'' +
                ", relativePosition=" + relativePosition +
                ", userName='" + userName + '\'' +
                ", status=" + status +
                ", userFinalImg='" + userFinalImg + '\'' +
                ", userTempImg='" + userTempImg + '\'' +
                '}';
    }

    /**
     * 序号 自增
     */
    @TableId(type = IdType.INPUT)
    private Integer id;
    /**
     * 合照的唯一识别号
     */
    private String photoId;
    /**
    * 用户的唯一openid
    */
    private String userOpenId;
    /**
    * 建立关系的时间
    */
    private Date createTime;
    /**
    * 用户上传的照片原图
    */
    private String userUploadedImg;
    /**
    * 用户的备注,不能太长,可以更改的
    */
    private String userRemark;
    /**
    * 用户的相对位置,初次由系统生成,后续由管理员调整
    */
    private Integer relativePosition;
    /**
    * 在当前合照里用户的名字,可以更改的
    */
    private String userName;
    /**
    * 状态,这里取3位,预留一些状态
    */
    private Integer status;
    /**
     * 用户的最终图片
     */
    private String userFinalImg;
    /**
     * 提取后的图片链接
     */
    private String userTempImg;

    public PhotoUserRelation(Integer id, String photoId, String userOpenId, Date createTime, String userUploadedImg, String userRemark, Integer relativePosition, String userName, Integer status, String userFinalImg, String userTempImg) {
        this.id = id;
        this.photoId = photoId;
        this.userOpenId = userOpenId;
        this.createTime = createTime;
        this.userUploadedImg = userUploadedImg;
        this.userRemark = userRemark;
        this.relativePosition = relativePosition;
        this.userName = userName;
        this.status = status;
        this.userFinalImg = userFinalImg;
        this.userTempImg = userTempImg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserUploadedImg() {
        return userUploadedImg;
    }

    public void setUserUploadedImg(String userUploadedImg) {
        this.userUploadedImg = userUploadedImg;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public Integer getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Integer relativePosition) {
        this.relativePosition = relativePosition;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUserFinalImg() {
        return userFinalImg;
    }

    public void setUserFinalImg(String userFinalImg) {
        this.userFinalImg = userFinalImg;
    }

    public String getUserTempImg() {
        return userTempImg;
    }

    public void setUserTempImg(String userTempImg) {
        this.userTempImg = userTempImg;
    }
}