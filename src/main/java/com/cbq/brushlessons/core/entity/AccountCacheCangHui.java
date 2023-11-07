package com.cbq.brushlessons.core.entity;

import lombok.Data;

/**
 * @description: 仓辉账号
 * @author 长白崎
 * @date 2023/10/23 14:55
 * @version 1.0
 */
@Data
public class AccountCacheCangHui implements AccountCache {
    private String session;
    private String code;
    private String token;
    private Integer status=0;
}
