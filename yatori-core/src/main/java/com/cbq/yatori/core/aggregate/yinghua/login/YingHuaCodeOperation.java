package com.cbq.yatori.core.aggregate.yinghua.login;

import com.cbq.yatori.core.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @description: TODO 用于检测验证码是否正确
 * @author 长白崎
 * @date 2024/5/23 14:57
 * @version 1.0
 */
@Slf4j
public class YingHuaCodeOperation extends YingHuaAbstractLogin{
    @Override
    public boolean action(YingHuaLogin loginAction, User user, Object o) {
        Map<String,Object> result = (Map<String,Object>)o;
        if(!(Boolean) result.get("status") && ((String) result.get("msg")).contains("验证码有误")){
            log.info("验证码错误");
            loginAction.action(user);
            return false;
        }
        log.info("验证码正确");
        return true;
    }
}
