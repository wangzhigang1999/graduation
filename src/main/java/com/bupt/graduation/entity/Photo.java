package com.bupt.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (Photo)实体类
 *
 * @author wangz
 * @since 2020-05-09 13:15:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo implements Serializable {
    private static final long serialVersionUID = 994969701913623455L;
    /**
     * id
     */
    @TableId(type = IdType.INPUT)
    private Integer id;
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

}