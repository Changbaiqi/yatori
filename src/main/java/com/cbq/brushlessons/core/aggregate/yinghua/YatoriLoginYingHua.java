package com.cbq.brushlessons.core.aggregate.yinghua;

import com.cbq.brushlessons.core.entity.User;
import lombok.Data;


@Data
public class YatoriLoginYingHua {
    private User user;
    public YatoriLoginYingHua(User user) {
        this.user = user;
    }
}
