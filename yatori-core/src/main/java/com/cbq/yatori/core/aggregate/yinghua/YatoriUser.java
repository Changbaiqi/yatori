package com.cbq.yatori.core.aggregate.yinghua;

import com.cbq.yatori.core.action.canghui.entity.loginresponse.LoginResponseRequest;
import com.cbq.yatori.core.action.enaea.entity.LoginAblesky;
import com.cbq.yatori.core.action.yinghua.LoginAction;
import com.cbq.yatori.core.entity.AccountCacheCangHui;
import com.cbq.yatori.core.entity.AccountCacheEnaea;
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
public class YatoriUser {
    User user;
    public YatoriLoginYingHua toLogin(){
        switch (user.getAccountType()) {
            //英华,创能
            case YINGHUA -> {
                AccountCacheYingHua accountCacheYingHua = new AccountCacheYingHua();
                user.setCache(accountCacheYingHua);
                //refresh_code:1代表密码错误，
                Map<String, Object> result = null;
                do {
                    //获取SESSION
                    String session = null;
                    while ((session = LoginAction.getSESSION(user)) == null) ;
                    accountCacheYingHua.setSession(session);
                    //获取验证码
                    File code = null;
                    while ((code = LoginAction.getCode(user)) == null) ;
                    accountCacheYingHua.setCode(VerificationCodeUtil.aiDiscern(code));
                    FileUtils.deleteFile(code);//删除验证码文件
                    //进行登录操作
                    while ((result = LoginAction.toLogin(user)) == null) ;
                } while (!(Boolean) result.get("status") && ((String) result.get("msg")).contains("验证码有误"));
                //对结果进行判定
                if ((Boolean) result.get("status")) {
                    accountCacheYingHua.setStatus(1);
                    log.info("{}登录成功！", user.getAccount());
                } else {
                    log.info("{}登录失败，服务器信息>>>{}", user.getAccount(), ((String) result.get("msg")));
                    return null;
                }

                //为账号维持登录状态-----------------------------------
                new Thread(() -> {
                    while (true) {
                        Map online;
                        //避免超时
                        while ((online = LoginAction.online(user)) == null) {
                            try {
                                Thread.sleep(1000 * 5);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        //如果含有登录超时字样
                        if (((String) online.get("msg")).contains("更新成功")) {
                            accountCacheYingHua.setStatus(1);
                        } else if (((String) online.get("msg")).contains("登录超时")) {
                            accountCacheYingHua.setStatus(2);//设定登录状态为超时
                            log.info("{}登录超时，正在重新登录...", user.getAccount());
                            //进行登录
                            Map<String, Object> map;
                            do {
                                //获取验证码
                                File code = LoginAction.getCode(user);
                                ((AccountCacheYingHua) user.getCache()).setCode(VerificationCodeUtil.aiDiscern(code));
                                FileUtils.deleteFile(code);//删除验证码文件
                                //进行登录操作
                                map = LoginAction.toLogin(user);
                            } while (!(Boolean) map.get("status") && ((String) map.get("msg")).contains("验证码有误"));
                            //对结果进行判定
                            if ((Boolean) map.get("status")) {
                                accountCacheYingHua.setStatus(1);
                                log.info("{}登录成功！", user.getAccount());
                            } else {
                                log.info("{}登录失败，服务器信息>>>{}", user.getAccount(), ((String) map.get("msg")));
                            }
                        }
                        try {
                            Thread.sleep(1000 * 60);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            }
            //仓辉
            case CANGHUI -> {
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
            }
            //学习公社
            case ENAEA -> {
                AccountCacheEnaea accountCacheEnaea = new AccountCacheEnaea();
                user.setCache(accountCacheEnaea);
                //sS:101代表账号或密码错误，
                LoginAblesky loginAblesky = null;
                while((loginAblesky=com.cbq.yatori.core.action.enaea.LoginAction.toLogin(user))==null);
                //对结果进行判定
                if (loginAblesky.getSS().equals("0")) {
                    accountCacheEnaea.setStatus(1);
                    log.info("{}登录成功！", user.getAccount());
                } else {
                    log.info("{}登录失败，服务器信息>>>{}", user.getAccount(), loginAblesky.getAlertMessage());
                    return null;
                }
            }

        }
        return new YatoriLoginYingHua(user);
    }
}
