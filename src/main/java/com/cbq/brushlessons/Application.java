package com.cbq.brushlessons;

import com.cbq.brushlessons.run.Launch;


public class Application {
    public static void main(String[] args) {
        Launch launch = new Launch();
        launch.init();
        launch.toRun();
    }
}