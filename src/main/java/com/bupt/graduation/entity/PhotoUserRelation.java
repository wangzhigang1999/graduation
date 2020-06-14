package com.bupt.graduation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (PhotoUserRelation)实体类
 *
 *
 * @author wangz
 * @since 2020-05-09 13:15:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoUserRelation implements Serializable {
    private static final long serialVersionUID = 643709644255110383L;
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



}