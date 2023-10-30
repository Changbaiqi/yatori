package com.cbq.brushlessons.core.utils;

import top.gcszhn.d4ocr.OCREngine;
import top.gcszhn.d4ocr.utils.IOUtils;

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
