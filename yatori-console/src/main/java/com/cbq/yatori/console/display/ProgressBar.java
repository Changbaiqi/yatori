package com.cbq.yatori.console.display;

public class ProgressBar extends Widget{
    private double now; //现在值
    private double max; //最大值
    private int progressBarLen; //进度条最大长度
    public ProgressBar(double now,double max,int progressBarLen) {
        this.now = now;
        this.max = max;
        this.progressBarLen = progressBarLen;

        for(int i =0;i<now/(max/progressBarLen);++i){
            this.stringBuilder.append('\u2588'); //添加进度条符号
        }
        this.stringBuilder.append(now/(max/100)+"%");
    }

}
