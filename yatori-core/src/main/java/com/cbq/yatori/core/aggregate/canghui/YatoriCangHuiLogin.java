package com.cbq.yatori.core.aggregate.canghui;

import com.cbq.yatori.core.aggregate.YatoriLogin;
import com.cbq.yatori.core.entity.User;

public class YatoriCangHuiLogin extends YatoriLogin {
    private User user;

    public YatoriCangHuiLogin(User user){
        this.user = user;
    }


    public CangHuiAllCoursesOperation getAllCoursesOperation(){
        return CangHuiAllCoursesOperation.builder().user(user).build();
    }


}
