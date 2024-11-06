package com.cbq.yatori.core.utils;

import com.cbq.yatori.core.utils.dddd.OCREngine;
import com.cbq.yatori.core.utils.dddd.utils.IOUtils;

import java.io.File;
import java.io.IOException;

public class VerificationCodeUtil {

    public static String aiDiscern(File file){
        OCREngine ocrEngine = OCREngine.instance();
        try {
           return ocrEngine.recognize(IOUtils.read(file.getPath()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
