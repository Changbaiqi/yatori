package com.cbq.yatori.console;

import com.cbq.yatori.console.run.Launch;


public class Application {
    public static void main(String[] args) {
        Launch launch = new Launch();
        launch.init();
        launch.toRun();
    }
}