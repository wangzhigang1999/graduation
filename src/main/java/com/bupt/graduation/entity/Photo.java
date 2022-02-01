package com.bupt.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * (Photo)实体类
 *
 * @author wangz
 * @since 2020-05-09 13:15:31
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = 994969701913623455L;
    /**
     * id
     */
    @TableId(type = IdType.INPUT)
    private Integer id;

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", photoId='" + photoId + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", classNumber='" + classNumber + '\'' +
                ", studentNumber=" + studentNumber +
                ", imageName='" + imageName + '\'' +
                ", creatorOpenId='" + creatorOpenId + '\'' +
                ", createDate=" + createDate +
                ", otherInfo='" + otherInfo + '\'' +
                ", backgroundImg='" + backgroundImg + '\'' +
                ", finalImg='" + finalImg + '\'' +
                ", status=" + status +
                '}';
    }

    /**
     * 唯一识别号,同时也是合照背景图片名称
     */
    private String photoId;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 班级名称
     */
    private String classNumber;
    /**
     * 学生数量
     */
    private Integer studentNumber;
    /**
     * 合照名称
     */
    private String imageName;
    /**
     * 创建者的 openid
     */
    private String creatorOpenId;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 其他需要说明的信息
     */
    private String otherInfo;
    /**
     * 背景图片
     */
    private String backgroundImg;
    /**
     * 最终合成的图片
     */
    private String finalImg;
    /**
     * 状态
     */
    private Integer status;

    public Photo(Integer id, String photoId, String schoolName, String classNumber, Integer studentNumber, String imageName, String creatorOpenId, Date createDate, String otherInfo, String backgroundImg, String finalImg, Integer status) {
        this.id = id;
        this.photoId = photoId;
        this.schoolName = schoolName;
        this.classNumber = classNumber;
        this.studentNumber = studentNumber;
        this.imageName = imageName;
        this.creatorOpenId = creatorOpenId;
        this.createDate = createDate;
        this.otherInfo = otherInfo;
        this.backgroundImg = backgroundImg;
        this.finalImg = finalImg;
        this.status = status;
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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getCreatorOpenId() {
        return creatorOpenId;
    }

    public void setCreatorOpenId(String creatorOpenId) {
        this.creatorOpenId = creatorOpenId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public String getFinalImg() {
        return finalImg;
    }

    public void setFinalImg(String finalImg) {
        this.finalImg = finalImg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}