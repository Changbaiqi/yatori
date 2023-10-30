package com.cbq.brushlessons.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @description: 用户类，每个操作单位将会以用户为基准进行操作
 * @author 长白崎
 * @date 2023/10/23 14:43
 * @version 1.0
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private AccountType accountType;
    private String url;
    private String account;
    private String password;
    private AccountCache cache;
}
