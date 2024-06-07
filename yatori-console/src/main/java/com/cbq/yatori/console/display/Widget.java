package com.cbq.yatori.console.display;

public abstract class Widget {
    protected StringBuilder stringBuilder = new StringBuilder();

    public String txt(){
        return stringBuilder.toString();
    }
}
