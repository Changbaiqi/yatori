package com.cbq.yatori.core.aggregate.yinghua.login;

import com.cbq.yatori.core.entity.User;

public abstract class YingHuaAbstractLogin {

    public abstract boolean action(YingHuaLogin loginAction, User user, Object o);
}
