# Graduation

![未命名文件](readme.assets/%E6%9C%AA%E5%91%BD%E5%90%8D%E6%96%87%E4%BB%B6.png)

## Run in K8S

> 使用Docker部署MySql&Redis

- Clone 项目到本地
- 修改部分ENV
    - `HTTP_UPLOAD_PATH` OSS地址 可选修改
    - `UPLOAD` OSS地址 可选修改
- 部署 ：`kubectl apply -f X.yaml`
    - Php Admin 可选部署，使用PHPAdmin可以更方便的建表&监控数据库状态
    - user=root password=root
- 根据sql.sql中的SQL建立数据库和表
- done

> 使用外部的MySql&Redis

- 修改application.yaml / 通过环境变量注入

## Run in local

- 部署MySql&Redis，根据sql.sql中的SQL建立数据库和表
- 设置环境变量 `UPLOAD_TARGET=OSS地址`
- 部署`Matting`服务
- done