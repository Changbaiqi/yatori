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


        String configText = FileUtils.readTxt(file);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Config config = objectMapper.readValue(configText, Config.class);
            return config;
        } catch (JsonProcessingException e) {
            System.out.println("ATRI：json配置文件写错啦，（╯#-皿-)╯~~╧═╧");
            System.out.println();

            throw new RuntimeException(e);
        }

    }
}
