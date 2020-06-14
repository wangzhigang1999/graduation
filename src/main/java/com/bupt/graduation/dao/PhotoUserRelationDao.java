package com.bupt.graduation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.graduation.entity.Photo;
import com.bupt.graduation.entity.PhotoUserRelation;
import javafx.util.Pair;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * (PhotoUserRelation)表数据库访问层
 *
 * @author wanz
 * @since 2020-05-09 13:16:27
 */

@Mapper
@Component
public interface PhotoUserRelationDao extends BaseMapper<PhotoUserRelation> {

    /**
     * 增加一条数据
     *
     * @param openId openId
     * @param uuid   uuid
     * @return 增加成功的条数
     */
    @Insert("insert ignore into graduation.photo_user_relation\n" +
            "    (photo_id, user_open_id, create_time)\n" +
            "VALUES (#{uuid}, #{openId}, now())\n ")
    Integer addRelation(String openId, String uuid);


    /**
     * 在上一步骤完成之后,用户完善自己的一部分信息
     *
     * @param openId  openid
     * @param uuid    uuid
     * @param name    name
     * @param remarks 备注
     * @return res
     */
    @Update("update graduation.photo_user_relation\n" +
            "set user_name=#{name},\n" +
            "    user_remark =#{remarks},\n" +
            "    status = \"2\" \n" +
            "where user_open_id= #{openId} and photo_id =#{uuid}")
    Integer addBasicInfo(String openId, String uuid, String name, String remarks);


    /**
     * 通过uuid和open id 获得一条记录
     *
     * @param uuid   uuid
     * @param openId openId
     * @return res
     */
    @Select("select status from photo_user_relation where photo_id = #{uuid} and user_open_id=#{openId}")
    Integer getStatusByUuidAndOpenId(String uuid, String openId);


    /**
     * 用户对自己的信息进行确认
     *
     * @param uuid   uuid
     * @param openId openId
     * @return res
     */
    @Update("update photo_user_relation set status =\"3\" where photo_id =#{uuid} and user_open_id=#{openId}")
    Integer confirmStatus(String uuid, String openId);


    /**
     * 用户上传图片后,将原图和处理过的图片链接都放进来
     *
     * @param uuid      uuid
     * @param openId    openId
     * @param localImg  fileName
     * @param remoteImg remote
     * @return res
     */
    @Update("update photo_user_relation\n" +
            "set user_uploaded_img = #{localImg},\n" +
            "    user_temp_img=#{remoteImg}\n" +
            "where user_open_id = #{openId}\n" +
            "  and photo_id =#{uuid}")
    Integer uploadImage(String uuid, String openId, String localImg, String remoteImg);


    /**
     * 用户上传最终的图片
     *
     * @param uuid     uuid
     * @param openId   openId
     * @param finalImg finalImg
     * @return res
     */
    @Update("update photo_user_relation\n" +
            "set user_final_img=#{finalImg}\n" +
            "where user_open_id = #{openId}\n" +
            "  and photo_id = #{uuid}")
    Integer uploadFinalImage(String uuid, String openId, String finalImg);


    /**
     * 由管理员手动更改用户的相对位置
     *
     * @param uuid     uuid
     * @param openId   openId
     * @param position position
     */
    @Update("update graduation.photo_user_relation " +
            "set relative_position =#{position} " +
            "where photo_id=#{uuid} " +
            "   and " +
            "user_open_id =#{openId}")
    void changePeopleOrder(String uuid, String openId, Integer position);


    /**
     * 获取uuid对应的所有的关系
     *
     * @param uuid uuid
     * @return res
     */
    @Select("select user_final_img , relative_position from photo_user_relation where photo_id =#{uuid}")
    List<Pair<String, Integer>> getImgAndPositionByUuid(String uuid);


    /**
     * 获取一些信息
     *
     * @param uuid uuid
     * @return res
     */
    @Select("select user_name, relative_position, user_remark,user_open_id\n" +
            "from graduation.photo_user_relation\n" +
            "where photo_id =#{uuid}")
    List<HashMap<String, String>> getOrders(String uuid);


    /**
     * 获取用户的最终照片
     *
     * @param uuid   uuid
     * @param openId openId
     * @return res
     */
    @Select("select  user_final_img from graduation.photo_user_relation where photo_id=#{uuid} and user_open_id=#{openId} ")
    String getFinalImage(String uuid, String openId);


    /**
     * 获取用户所有的合照信息
     *
     * @param openId openId
     * @return res
     */
    @Select("select *\n" +
            "from photo\n" +
            "where photo_id in\n" +
            "      (select photo_id\n" +
            "       from photo_user_relation\n" +
            "       where user_open_id = #{openId} )")
    List<Photo> getUserPhotosByOpenId(String openId);


    /**
     * 获取一张合照的简要信息,在用户被邀请的时候调用
     *
     * @param uuid uuid
     */
    @Select("select id,\n" +
            "       photo_id,\n" +
            "       school_name,\n" +
            "       class_number,\n" +
            "       student_number,\n" +
            "       image_name,\n" +
            "       creator_open_id,\n" +
            "       create_date,\n" +
            "       other_info,\n" +
            "       background_img,\n" +
            "       final_img,\n" +
            "       status\n" +
            "from photo\n" +
            "where photo_id=#{uuid}")
    Photo getPhotoInfoByInvitationCode(String uuid);
}