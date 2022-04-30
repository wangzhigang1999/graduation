package com.bupt.graduation.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.util.Map;
import java.util.UUID;

/**
 * Created by wangzhigang on 2022/5/1 12:28 AM
 */
public class OSS {

    private static final COSClient client;
    private static final String bucketName = "bkt-1259077154";


    static {
        Map<String, String> env = System.getenv();
        // 1 初始化用户身份信息（secretId, secretKey）。
        // SECRETID和SECRETKEY请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        String secretId = env.get("SECRETID");
        String secretKey = env.get("SECRETKEY");
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(env.get("COS_REGION"));
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        client = new COSClient(cred, clientConfig);
    }

    public static boolean uploadToOss(File file, String fileName) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
        PutObjectResult putObjectResult = client.putObject(putObjectRequest);
        return true;
    }

    public static boolean download(String uuid) {
        // 方法2 下载文件到本地的路径，例如 D 盘的某个目录
        String outputFilePath = "./test.jar";
        File downFile = new File(outputFilePath);
        var getObjectRequest = new GetObjectRequest(bucketName, uuid);
        ObjectMetadata downObjectMeta = client.getObject(getObjectRequest, downFile);
        System.out.println(downObjectMeta.getContentMD5());
        return true;
    }

    public static void main(String[] args) {

        // 指定要上传的文件
        File localFile = new File("/Users/wangzhigang/IdeaProjects/graduation/sql.sql");
        // 指定文件将要存放的存储桶
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        long start = System.currentTimeMillis();
        String key = UUID.randomUUID().toString();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
        PutObjectResult putObjectResult = client.putObject(putObjectRequest);
        System.out.println(putObjectResult);
        System.out.println(System.currentTimeMillis() - start);

        download(key);

    }

}
