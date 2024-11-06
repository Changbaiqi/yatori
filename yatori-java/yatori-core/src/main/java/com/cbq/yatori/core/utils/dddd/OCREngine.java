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
package com.cbq.yatori.core.utils.dddd;

import java.awt.image.BufferedImage;

/**
 * 统一的OCR引擎接口，所有引擎扩展需要实现该接口
 * @author GCS-ZHN
 * */
public interface OCREngine {
    /**
     * OCR文本识别的抽象API
     * @param image 图像
     * @return 识别的文本
     */
    public String recognize(BufferedImage image);
    /**
     * 返回D4 OCR引擎实例
     * */
    public static OCREngine instance() {
        return new OCREngineOldImpl();
    }
}
