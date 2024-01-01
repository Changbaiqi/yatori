package com.cbq.brushlessons.core.aggregate;

import com.cbq.brushlessons.core.entity.User;

public abstract class YatoriUser {
    User user;

    public abstract YatoriLogin toLogin();
}
