package com.cbq.yatori.core.aggregate;

import com.cbq.yatori.core.entity.User;

public abstract class YatoriUser {
    User user;

    public abstract YatoriLogin toLogin();
}
