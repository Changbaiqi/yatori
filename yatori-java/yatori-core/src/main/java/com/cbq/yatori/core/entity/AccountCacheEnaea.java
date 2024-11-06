package com.cbq.yatori.core.entity;

import lombok.Data;

@Data
public class AccountCacheEnaea implements AccountCache{
    private String ASUSS; //相当于token
    private Integer status=0; //账号状态

}
