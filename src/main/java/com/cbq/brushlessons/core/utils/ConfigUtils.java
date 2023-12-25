package com.cbq.brushlessons.core.utils;

import com.cbq.brushlessons.core.entity.Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ConfigUtils {
    public static Config loadingConfig(){


        File file = new File("./config.json");
        if(!file.exists()){
//            System.out.println("配置文件不存在!!!");
            log.error("亚托莉：你把配置文件丢哪去了，我根本读不到！（╯#-皿-)╯~~╧═╧");
            return null;
        }


        String configText =FileUtils.readTxt(file);


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Config config = objectMapper.readValue(configText, Config.class);
            return config;
        } catch (JsonProcessingException e) {
            log.error("亚托莉：json配置文件写错啦！如果没写错那么可能是你配置文件编码问题，注意一定要用UTF-8编码！（╯#-皿-)╯~~╧═╧");
            throw new RuntimeException(e);
        }

    }
}
