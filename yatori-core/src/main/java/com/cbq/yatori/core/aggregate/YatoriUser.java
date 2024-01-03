package com.cbq.yatori.core.aggregate;

import com.cbq.yatori.core.entity.User;
import lombok.Builder;


public abstract class YatoriUser {

    public abstract YatoriLogin toLogin();
}
