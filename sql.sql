create database graduation;

use graduation;

create table if not exists photo
(
    id              int auto_increment
        primary key,
    photo_id        varchar(255)  not null,
    school_name     varchar(255)  null,
    class_number    varchar(255)  null,
    student_number  int           null,
    image_name      varchar(255)  null,
    creator_open_id varchar(255)  null,
    create_date     datetime      null,
    other_info      text          null,
    background_img  varchar(255)  null,
    final_img       varchar(255)  null,
    status          int default 0 not null,
    constraint photo_photo_id_uindex
        unique (photo_id)
);

create table if not exists photo_user_relation
(
    id                int auto_increment
        primary key,
    photo_id          varchar(255)  null,
    user_open_id      varchar(255)  null,
    create_time       datetime      null,
    user_uploaded_img varchar(255)  null,
    user_remark       varchar(255)  null,
    relative_position int           null,
    user_name         varchar(255)  null,
    status            int default 0 not null,
    user_final_img    varchar(255)  null,
    user_temp_img     varchar(255)  null
);

