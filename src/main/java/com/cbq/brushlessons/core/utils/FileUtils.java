package com.cbq.brushlessons.core.utils;

import java.io.*;

/**
 * @description: 文件相关操作
 * @author 长白崎
 * @date 2023/10/23 15:42
 * @version 1.0
 */
public class FileUtils {
    private final static String PATH="cache";

    static {
        File file = new File(PATH);
        if(!file.exists()){
            file.mkdir();
        }
    }
    public static File saveFile(byte bytes[], String fileName) {
        try {
            InputStream in = new ByteArrayInputStream(bytes);
            FileOutputStream file = new FileOutputStream(PATH+"/"+fileName);

            int j;
            while ((j = in.read()) != -1) {
                file.write(j);
            }
            file.flush();
            file.close();
            in.close();
            File file1 = new File(PATH+"/"+fileName);
            return file1;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除文件
     * @param file
     * @return
     */
    public static boolean deleteFile(File file){
        if(file.isFile() && file.exists()){
            file.delete();
            return true;
        }
        return false;
    }
}
