package com.cbq.yatori.core.aggregate.canghui;

import com.cbq.yatori.core.action.canghui.entity.loginresponse.LoginResponseRequest;
import com.cbq.yatori.core.aggregate.YatoriLogin;
import com.cbq.yatori.core.aggregate.YatoriUser;
import com.cbq.yatori.core.entity.AccountCacheCangHui;
import com.cbq.yatori.core.entity.AccountCacheYingHua;
import com.cbq.yatori.core.entity.User;
import com.cbq.yatori.core.utils.FileUtils;
import com.cbq.yatori.core.utils.VerificationCodeUtil;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Map;

@Slf4j
@Builder
public class YatoriCangHuiUser extends YatoriUser {
    private User user;


    @Override
    public YatoriCangHuiLogin toLogin() {

        AccountCacheCangHui accountCacheCangHui = new AccountCacheCangHui();
        user.setCache(accountCacheCangHui);
        //refresh_code:1代表密码错误，
        LoginResponseRequest result=null;
        do {
            //获取SESSION
            String session = null;
            while ((session = com.cbq.yatori.core.action.canghui.LoginAction.getSESSION(user)) == null) ;
            accountCacheCangHui.setSession(session);
            //获取验证码
            File code = null;
            while ((code = com.cbq.yatori.core.action.canghui.LoginAction.getCode(user)) == null) ;
            accountCacheCangHui.setCode(VerificationCodeUtil.aiDiscern(code));
            FileUtils.deleteFile(code);//删除验证码文件
            //进行登录操作
            while ((result = com.cbq.yatori.core.action.canghui.LoginAction.toLogin(user)) == null) ;
        } while (result.getCode()==-1002);
        //对结果进行判定
        if (result.getCode()==0) {
            accountCacheCangHui.setToken(result.getData().getToken());
            accountCacheCangHui.setStatus(1);
            log.info("{}登录成功！", user.getAccount());
        } else {
            log.info("{}登录失败，服务器信息>>>{}", user.getAccount(), result.getMsg());
            return null;
        }


        //为账号维持登录状态-----------------------------------
        new Thread(() -> {
            while (true) {
                Map online;
                //避免超时
                while ((online = com.cbq.yatori.core.action.canghui.LoginAction.online(user)) == null) {
                    try {
                        Thread.sleep(1000 * 5);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                //如果含有登录超时字样
                if (((String) online.get("msg")).contains("成功")) {
                    accountCacheCangHui.setStatus(1);
                } else if (((String) online.get("msg")).contains("登录超时")) {
                    accountCacheCangHui.setStatus(2);//设定登录状态为超时
                    log.info("{}登录超时，正在重新登录...", user.getAccount());
                    //进行登录
                    LoginResponseRequest map=null;
                    do {
                        //获取验证码
                        File code = com.cbq.yatori.core.action.canghui.LoginAction.getCode(user);
                        ((AccountCacheYingHua) user.getCache()).setCode(VerificationCodeUtil.aiDiscern(code));
                        FileUtils.deleteFile(code);//删除验证码文件
                        //进行登录操作
                        while ((map=com.cbq.yatori.core.action.canghui.LoginAction.toLogin(user))==null);
                    } while (map.getCode()==-1002);
                    //对结果进行判定
                    if (map.getCode()==0) {
                        accountCacheCangHui.setStatus(1);
                        log.info("{}登录成功！", user.getAccount());
                    } else {
                        log.info("{}登录失败，服务器信息>>>{}", user.getAccount(), map.getMsg());
                    }
                }
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


        return null;
    }
}
