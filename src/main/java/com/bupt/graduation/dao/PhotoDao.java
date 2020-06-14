package com.bupt.graduation.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bupt.graduation.entity.Photo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

/**
 * (Photo)表数据库访问层
 *
 * @author wanz
 * @since 2020-05-09 13:15:31
 */
@Mapper
@Component
public interface PhotoDao extends BaseMapper<Photo> {

    /**
     * 获取合照的链接
     *
     * @param uuid uuid
     * @return res
     */
    @Select("select final_img from graduation.photo where photo_id=#{uuid}")
    String getFinalPhotoLink(String uuid);

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

    @Insert("insert into graduation.photo (photo_id, school_name, class_number, student_number,\n" +
            "                              image_name, creator_open_id, create_date, other_info)\n" +
            "VALUES (#{uuid}, #{schoolName}, #{className}, #{studentNumber}, #{imgName}, #{openId}, now(), #{otherInfo})")
    Integer createBasicInfo(String uuid, String imgName, String schoolName, String className,
                            Integer studentNumber, String otherInfo, String openId);

    /**
     * 用于改变合照的信息
     *
     * @param uuid       uuid
     * @param imgName    合照的名称
     * @param schoolName 学校名称
     * @param className  班级名称
     * @param otherInfo  其他的一些信息
     * @return res
     */
    @Update("update graduation.photo\n" +
            "set image_name=#{imgName},\n" +
            "    school_name=#{schoolName},\n" +
            "    class_number=#{className},\n" +
            "    other_info=#{otherInfo}\n" +
            "where photo_id = #{uuid}")
    Integer changeBasicInfo(String uuid, String imgName, String schoolName, String className,
                            String otherInfo);


    /**
     * 上传合照的背景
     *
     * @param uuid      uuid
     * @param bgImgName bgImgName
     * @return res
     */
    @Update("update  graduation.photo set background_img = #{bgImgName} where photo_id=#{uuid}")
    Integer uploadBackgroundImg(String uuid, String bgImgName);


    /**
     * 根据 uuid 来获得 id ,以此来判断一个记录是否存在
     *
     * @param uuid uuid
     * @return res
     */
    @Select("select id from graduation.photo where photo_id=#{uuid}")
    Long getIdByUuid(String uuid);


    /**
     * 改变合照的状态
     *
     * @param uuid   uuid
     * @param status 状态
     * @return 执行的结果条数
     */
    @Update("update graduation.photo set status=#{status} where photo_id=#{uuid}")
    Integer changeStatus(String uuid, Integer status);


    @Update("update graduation.photo set " +
            "status=#{status} ," +
            "final_img=#{img} where photo_id=#{uuid}")
    Integer releaseConfirm(String uuid, Integer status, String img);


    @Select("select  status from graduation.photo where photo_id=#{uuid}")
    Integer getStatus(String uuid);
}