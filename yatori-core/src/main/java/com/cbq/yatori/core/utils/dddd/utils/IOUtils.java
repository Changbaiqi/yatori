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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * IO工具类
 */
public class IOUtils {
    /**
     * 将JAR包内资源提取出来
     * @param source jar内部文件URI
     * @param target 目标文件名
     * @throws IOException
     */
    public static void extractJarResource(String source, File target) throws IOException {
        try(InputStream in = IOUtils.class.getResourceAsStream(source);
            FileOutputStream out = new FileOutputStream(target)) {
            byte[] buffer = new byte[1 << 11];
            int len;
            while ((len = in.read(buffer)) != -1){
                out.write(buffer, 0, len);
                out.flush();
            }
        }
    }
    /**
     * 将图像保存为文件
     * @param image BufferedImage图像
     * @param formatName 图片格式，如jpg等
     * @param file 输出文件
     */
    public static void write(BufferedImage image, String formatName, File file) {
        try {
            ImageIO.write(image, formatName, file);
        } catch (Exception e) {
            LogUtils.printMessage(null, e, LogUtils.Level.ERROR);
        }
    }

    /**
     * 将字节数组储存到文件
     * @param byteArray 字节数组
     * @param file 输出文件
     */
    public static void write(byte[] byteArray, File file) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(byteArray);
        } catch (Exception e) {
            LogUtils.printMessage(null, e, LogUtils.Level.ERROR);
        }
    }

    /**
     * 将base64编码数据解码并储存到文件
     * @param base64 编码字符串
     * @param file 输出文件
     */
    public static void write(String base64, File file) {
        try {
            write(Base64.getDecoder().decode(base64), file);
        } catch (Exception e) {
            LogUtils.printMessage(null, e, LogUtils.Level.ERROR);
        }
    }

    /**
     * 读取图像文件
     * @param imageFile 图像文件
     * @return 图像对象
     * @throws IOException
     */
    public static BufferedImage read(String imageFile) throws IOException {
        return ImageIO.read(new File(imageFile));
    }
}
