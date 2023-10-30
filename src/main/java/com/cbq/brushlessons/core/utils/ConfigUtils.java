package com.cbq.brushlessons.core.utils;

import com.cbq.brushlessons.core.entity.Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigUtils {
    public static Config loadingConfig(){
        File file = new File("./config.json");
        if(!file.exists()){
            System.out.println("配置文件不存在!!!");
            return null;
        }

        String configText = "";
        try {
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
            //FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String res = null;
            while((res = br.readLine())!=null){
                configText+=res;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(config);


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Config config = objectMapper.readValue(configText, Config.class);
            return config;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
