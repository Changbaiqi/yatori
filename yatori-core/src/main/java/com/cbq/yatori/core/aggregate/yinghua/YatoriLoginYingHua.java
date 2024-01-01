package com.cbq.yatori.core.aggregate.yinghua;

import com.cbq.yatori.core.entity.User;
import lombok.Data;


@Data
public class YatoriLoginYingHua {
    private User user;
    public YatoriLoginYingHua(User user) {
        this.user = user;
    }
}
