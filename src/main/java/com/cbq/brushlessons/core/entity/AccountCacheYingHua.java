package com.cbq.brushlessons.core.entity;

import lombok.Data;

@Data
public class AccountCacheYingHua implements AccountCache {
    private String session;
    private String token;
    private String code;
}
