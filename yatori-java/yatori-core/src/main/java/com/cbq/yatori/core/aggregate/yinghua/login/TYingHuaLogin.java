package com.cbq.yatori.core.aggregate.yinghua.login;

import com.cbq.yatori.core.action.yinghua.LoginAction;
import com.cbq.yatori.core.entity.AccountCacheYingHua;
import com.cbq.yatori.core.entity.User;
import com.cbq.yatori.core.utils.FileUtils;
import com.cbq.yatori.core.utils.VerificationCodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Map;

@Slf4j
public class TYingHuaLogin {
    private User user;
    public TYingHuaLogin(User user){
        this.user =user;
    }
    /**
     * 登录
     */
    public Map<String,Object> login(){
        AccountCacheYingHua accountCacheYingHua = new AccountCacheYingHua(); //获取账号信息缓存
        user.setCache(accountCacheYingHua);
        //refresh_code:1代表密码错误，
        Map<String, Object> result = null;
        //获取SESSION
        String session = null;
        while ((session = LoginAction.getSESSION(user)) == null) ; //获取登录用的SESSION
        accountCacheYingHua.setSession(session); //设置SESSION
        //获取验证码
        File code = null; //验证码图片所在路径
        while ((code = LoginAction.getCode(user)) == null) ; //获取验证码图片所在路径
        accountCacheYingHua.setCode(VerificationCodeUtil.aiDiscern(code)); //进行AI识别验证码
        FileUtils.deleteFile(code);//删除验证码文件
        //进行登录操作
        while ((result = LoginAction.toLogin(user)) == null) ; //进行登录操作
        return result;
    }

    /**
     * 验证码识别
     */
    public void code(Map<String,Object> loginResult){
        if(!(Boolean) loginResult.get("status") && ((String) loginResult.get("msg")).contains("验证码有误")){
            log.info("验证码错误");
            login(); //重新登录
        }
        log.info("验证码正确");
    }


    public static void toLogin(User user){
        TYingHuaLogin tYingHuaLogin = new TYingHuaLogin(user);
        Map<String, Object> login = tYingHuaLogin.login(); //登录
        tYingHuaLogin.code(login); //识别验证码
    }
}
