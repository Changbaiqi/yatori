package com.cbq.yatori.core.aggregate.yinghua.login;

import com.cbq.yatori.core.entity.User;

public abstract class YingHuaAbstractLogin {
    private YingHuaAbstractLogin nextAction;
    protected Object result;
    public void setNextAction(YingHuaAbstractLogin nextAction)
    {
        this.nextAction = nextAction;
    }

    public abstract boolean action(YingHuaLogin loginAction, User user, Object o);

    public Object getResult() {
        return result;
    }
}
