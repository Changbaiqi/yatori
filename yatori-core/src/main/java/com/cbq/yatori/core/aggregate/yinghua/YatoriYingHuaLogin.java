package com.cbq.yatori.core.aggregate.yinghua;

import com.cbq.yatori.core.aggregate.YatoriLogin;
import com.cbq.yatori.core.entity.User;
import lombok.Data;


@Data
public class YatoriYingHuaLogin extends YatoriLogin {
    private User user;
    public YatoriYingHuaLogin(User user) {
        this.user = user;
    }

    @Override
    public YatoriYingHuaOperation getOperation() {

        return null;
    }
}
