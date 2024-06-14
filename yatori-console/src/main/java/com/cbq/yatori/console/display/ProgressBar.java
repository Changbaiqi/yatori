package com.cbq.yatori.console.display;

/**
 * 进度条类
 */
public class ProgressBar extends Widget {

    public ProgressBar(double now, double max, int progressBarLen) {
        for (int i = 0; i < now / (max / progressBarLen); ++i) {
            this.stringBuilder.append('█'); //添加进度条符号
        }
        this.stringBuilder.append(now / (max / 100)).append("%");
    }

}
