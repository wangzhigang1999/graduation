package com.bupt.graduation.utils;

import com.bupt.graduation.entity.WidthAndHeight;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;

/**
 * 图像合成工具类
 *
 * @author wangz
 */
public class ImageConcatUtil {


    /**
     * 合成的图片的保存目录
     */
    private final static String PATH = System.getProperties().getProperty("user.dir") + "/IMG";


    private final static HashMap<Integer, WidthAndHeight> BACKGROUND_MAP = new HashMap<>();


    static {
        init();
        MakeDirUtil.mkDir(PATH);
    }


    /**
     * 合成图片
     *
     * @param inputImg 输入的带合成的图片的数组
     * @return 合成后的图片的名称
     */
    public static String concat(String[] inputImg) {
        try {

            int length = inputImg.length;

            if (length <= 0) {
                throw new IndexOutOfBoundsException("The array length of the composite picture cannot be less than 0 or equal to 0!");
            }


            WidthAndHeight bgSize = BACKGROUND_MAP.get(length);

            // 创建透明的背景
            BufferedImage background = createTransparentBackground(bgSize.getWidth(), bgSize.getHeight());

            Graphics2D graphics2D = background.createGraphics();


            BufferedImage[] images = new BufferedImage[length];


            // 读取本地文件
            for (int i = 0; i < inputImg.length; i++) {
                images[i] = ImageIO.read(new File(inputImg[i]));
            }


            int x = 0;
            int y = 0;

            // 开始合成
            for (BufferedImage image : images) {
                graphics2D.drawImage(image, x, y, 288, 600, null);
                x += 170;
            }

            graphics2D.dispose();

            return ImageUploadUtil.saveOnline(getImageStream(background));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建一张透明的背景图片
     *
     * @param width  宽
     * @param height 高
     * @return BufferedImage
     */
    private static BufferedImage createTransparentBackground(int width, int height) {

        BufferedImage background = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = background.createGraphics();
        background = graphics.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        graphics.dispose();
        return background;
    }

    private static void init() {
        BACKGROUND_MAP.put(1, new WidthAndHeight(288, 600));
        BACKGROUND_MAP.put(2, new WidthAndHeight(458, 600));
        BACKGROUND_MAP.put(3, new WidthAndHeight(628, 600));
        BACKGROUND_MAP.put(4, new WidthAndHeight(798, 600));
        BACKGROUND_MAP.put(5, new WidthAndHeight(968, 600));

    }

    private static InputStream getImageStream(BufferedImage image) {
        InputStream stream = null;
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut;
        try {
            imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(image, "png", imOut);
            stream = new ByteArrayInputStream(bs.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

}


