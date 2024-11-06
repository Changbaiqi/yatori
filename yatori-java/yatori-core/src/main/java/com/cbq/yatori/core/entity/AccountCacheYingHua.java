package com.cbq.yatori.core.entity;

import lombok.Data;

@Data
public class AccountCacheYingHua implements AccountCache {
    private String session;
    private String token;
    private String code;
    /**
     * 新添加账号状态，0代表未登录，1代表正常登录，2代表登录超时
     */
    private Integer status=0;
}
