package com.bupt.graduation.utils;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

/**
 * 利用 Baidu 提供的 api 将人像整体提取出来
 * 部分东西需要替换成你自己的
 *
 * @author wangz
 */
public class ImageSegmentationFull {
    /**
     * Baidu的 app id
     */
    private static final String APP_ID = "replace";
    /**
     * Baidu的app key
     */
    private static final String API_KEY = "replace";
    /**
     * Baidu
     */
    private static final String SECRET_KEY = "replace";

    /**
     * 提取的人像的保存目录
     */
    private final static String PATH = System.getProperties().getProperty("user.dir") + "/IMG";

    /**
     * 人像提取的请求客户端
     */
    private static final AipBodyAnalysis CLIENT = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);
    /**
     * 请求的各项参数
     */
    private static final HashMap<String, String> OPTIONS;

    /**
     * 请求的种类
     */
    private final static String TYPE = "foreground";

    static {
        CLIENT.setConnectionTimeoutInMillis(2000);
        CLIENT.setSocketTimeoutInMillis(60000);

        OPTIONS = new HashMap<>();

        OPTIONS.put("type", TYPE);

        MakeDirUtil.mkDir(PATH);

    }


    /**
     * 获取提取完毕的图像链接
     *
     * @param img 待提取的本图像的二进制形式
     * @return 提取完毕的图片链接
     */
    public static String getImgUrl(byte[] img) {

        JSONObject remoteRes = CLIENT.bodySeg(img, ImageSegmentationFull.OPTIONS);

        String fileName = UUID.randomUUID().toString();

        return writeImgAndGetPath(remoteRes, fileName);
    }


    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param base64 图片的base64格式字符串
     * @param path   输出文件的位置
     * @return 是否成功
     */
    private static boolean base64ToImageFile(String base64, String path) throws IOException {
        try {
            OutputStream out = new FileOutputStream(path);
            return base64ToImageOutput(base64, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean base64ToImageOutput(String base64, OutputStream out) throws IOException {
        // 图像数据为空
        if (base64 == null) {
            return false;
        }
        try {
            // Base64解码
            byte[] bytes = Base64.getDecoder().decode(base64);
            return write(out, bytes);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean write(OutputStream out, byte[] bytes) throws IOException {
        for (int i = 0; i < bytes.length; ++i) {
            // 调整异常数据
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        // 生成jpeg图片
        out.write(bytes);
        out.flush();
        return true;
    }

    /**
     * @param res  从百度返回的数据 json格式
     * @param name 图片将要保存的位置
     * @return 图片保存的路径, 错误时为 null
     */
    private static String writeImgAndGetPath(JSONObject res, String name) {
        try {
            // 默认的图片文件是 jpeg 格式的
            boolean foreground = base64ToImageFile(res.get(TYPE).toString(), PATH + "/" + name + ".jpeg");
            if (!foreground) {
                return null;
            }
        } catch (Exception ignored) {
            return null;
        }
        return name + ".jpeg";
    }


}