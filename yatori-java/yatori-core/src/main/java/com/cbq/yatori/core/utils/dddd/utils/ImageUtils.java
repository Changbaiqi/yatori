/*
 * Copyright © 2022 <a href="mailto:zhang.h.n@foxmail.com">Zhang.H.N</a>.
 *
 * Licensed under the Apache License, Version 2.0 (thie "License");
 * You may not use this file except in compliance with the license.
 * You may obtain a copy of the License at
 *
 *       http://wwww.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language govering permissions and
 * limitations under the License.
 */
package com.cbq.yatori.core.utils.dddd.utils;



import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * 图像处理工具类
 */
public class ImageUtils {
    /**
     * 图像大小变换
     * @param image 旧图
     * @param width 宽度
     * @param height 高度
     * @return 新图
     */
    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Graphics2D g2d = newImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return newImage;
    }

    /**
     * 将图像进行base64编码
     * @param image 图像
     * @param formatName 图像格式，如jpg等
     * @return 编码的字符串
     */
    public static String toBase64(BufferedImage image, String formatName) {
        try {
            return Base64.getEncoder().encodeToString(toByteArray(image, formatName));
        } catch (Exception e) {
            LogUtils.printMessage(null, e, LogUtils.Level.ERROR);
        }
        return null;
    }

    /**
     * 将图像转为字节数组
     * @param image 图像
     * @param formatName 图像格式
     * @return 产生的字节数组
     */
    public static byte[] toByteArray(BufferedImage image, String formatName) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, formatName , outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            LogUtils.printMessage(null, e, LogUtils.Level.ERROR);
        }
        return null;
    }

    /**
     * 将base64编码的图像进行解码
     * @param base64 编码字符串
     * @return 解码产生的图像实例
     */
    public static BufferedImage toImage(String base64) {
        try  {
            return toImage(Base64.getDecoder().decode(base64));
        } catch (Exception e) {
            LogUtils.printMessage(null, e, LogUtils.Level.ERROR);
        }
        return null;
    }

    /**
     * 将字节数组转化为图像
     * @param byteArray 字节数组信息
     * @return 产生的图像
     */
    public static BufferedImage toImage(byte[] byteArray) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray)) {
            return ImageIO.read(inputStream);
        } catch (Exception e) {
            LogUtils.printMessage(null, e, LogUtils.Level.ERROR);
        }
        return null;
    }

    /**
     * 将图像进行灰度处理
     * @param image 原始图像
     * @return 灰度图像
     */
    public static BufferedImage toGray(BufferedImage image) {
        BufferedImage target = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = target.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return target;
    }

    /**
     * 将图像二值化处理
     * @param image 原始图像
     * @return 二值化图像
     */
    public static BufferedImage toBinary(BufferedImage image) {
        BufferedImage target = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = target.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return target;
    }
}
