package com.cbq.brushlessons;

import com.cbq.brushlessons.core.action.yinghua.CourseAction;
import com.cbq.brushlessons.core.action.yinghua.CourseStudyAction;
import com.cbq.brushlessons.core.action.yinghua.LoginAction;
import com.cbq.brushlessons.core.action.yinghua.entity.allcourse.CourseRequest;
import com.cbq.brushlessons.core.entity.AccountCacheYingHua;
import com.cbq.brushlessons.core.entity.AccountType;
import com.cbq.brushlessons.core.entity.User;
import com.cbq.brushlessons.core.utils.VerificationCodeUtil;
import com.cbq.brushlessons.run.Launch;

import java.io.File;

public class Application {
    public static void main(String[] args) {
        Launch launch = new Launch();
        launch.init();
        launch.toRun();
    }
}