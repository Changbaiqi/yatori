package com.cbq.yatori.core.aggregate;

import com.cbq.yatori.core.aggregate.yinghua.login.YingHuaAbstractLogin;
import com.cbq.yatori.core.entity.User;

/**
 * @description: TODO 登录器抽象类
 * @author 长白崎
 * @date 2024/5/23 15:14
 * @version 1.0
 */
public abstract class YatoriLogin {
    public abstract void action(User user);
}
