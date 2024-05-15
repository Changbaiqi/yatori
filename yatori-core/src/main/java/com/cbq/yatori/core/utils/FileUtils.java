package com.cbq.yatori.core.utils;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.nio.file.Files;

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

    /**
     * 读取指定路径下的文本文件并返回文本字符串
     * @param file
     * @return
     */
    public static String readTxt(File file){
        if(!file.exists()){
            System.out.println("配置文件不存在!!!");
            return null;
        }

        String text = "";
        try {
            InputStream inputStream = new FileInputStream(file);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,getCharsetName(file));
            //FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String res = null;
            while((res = br.readLine())!=null){
                text+=res;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return text;
    }

    /**
     * 获取文件编码字符串
     *
     * @param file
     * @return
     */
    public static String getCharsetName(File file) throws IOException {
        InputStream is = Files.newInputStream(file.toPath());
        BufferedInputStream reader = new BufferedInputStream(is);
        byte[] buff = new byte[1024];
        int len = 0;
//      检测文件编码
        UniversalDetector detector = new UniversalDetector(null);
        while ((len = reader.read(buff)) != -1 && !detector.isDone()) {
            detector.handleData(buff, 0, len);
        }
        detector.dataEnd();
//      获取编码类型
        String encoding = detector.getDetectedCharset();
        detector.reset();
        reader.close();
        return encoding;
    }
}
