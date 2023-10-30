package com.cbq.brushlessons;

import com.cbq.brushlessons.core.action.yinghua.CourseAction;
import com.cbq.brushlessons.core.action.yinghua.CourseStudyAction;
import com.cbq.brushlessons.core.action.yinghua.LoginAction;
import com.cbq.brushlessons.core.action.yinghua.entity.allcourse.CourseRequest;
import com.cbq.brushlessons.core.entity.AccountCacheYingHua;
import com.cbq.brushlessons.core.entity.AccountType;
import com.cbq.brushlessons.core.entity.User;
import com.cbq.brushlessons.core.utils.VerificationCodeUtil;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        //构建用户信息
        User user = new User();
        user.setType(AccountType.YINGHUA);
        user.setUrl("https://mooc.bwgl.cn/");
        user.setAccount("2151118");
        user.setPassword("02Y4Qtvk");

        AccountCacheYingHua accountYingHua = new AccountCacheYingHua();

        user.setCache(accountYingHua);



        //获取SESSION
        String session = LoginAction.getSESSION(user);
        accountYingHua.setSession(session);

        //获取验证码
        File code = LoginAction.getCode(user);
        String s = VerificationCodeUtil.aiDiscern(code);
        accountYingHua.setCode(s);
        System.out.println(s);

        //进行登录操作
        LoginAction.toLogin(user);
        System.out.println(session);
        accountYingHua.setToken("sid.mxfae22EW8UlV0lGje9hQtQXaQNiln");

        //获取全部的课表请求
        CourseRequest allCourseList = CourseAction.getAllCourseRequest(user);

        //将第一门课加入学习
        CourseStudyAction build = CourseStudyAction.builder()
                .user(user)
                .courseInform(allCourseList.getResult().getList().get(6))
                .build();
        build.toStudy();

    }
}