package com.cbq.yatori.core.aggregate;

import com.cbq.yatori.core.aggregate.yinghua.YingHuaUser;
import com.cbq.yatori.core.aggregate.yinghua.login.YingHuaLogin;
import com.cbq.yatori.core.entity.User;

public enum AccountType {
    YINGHUA(1,"YINGHUA"){
        @Override
        public YatoriLogin login(User user) {
            return YingHuaLogin.loginAction(user);
        }
    }; //英华


    Integer code; ///编号
    String platformName; //平台名称


    AccountType(Integer code,String platformName){
        this.code = code;
        this.platformName = platformName;
    }

    public abstract YatoriLogin login(User user);

    public Integer getCode() {
        return code;
    }

    public String getPlatformName() {
        return platformName;
    }
}
