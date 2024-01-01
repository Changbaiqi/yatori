package com.cbq.brushlessons.core.aggregate.yinghua;

import com.cbq.brushlessons.core.action.yinghua.LoginAction;
import com.cbq.brushlessons.core.aggregate.YatoriLogin;
import com.cbq.brushlessons.core.aggregate.YatoriUser;
import com.cbq.brushlessons.core.entity.AccountCacheYingHua;
import com.cbq.brushlessons.core.utils.FileUtils;
import com.cbq.brushlessons.core.utils.VerificationCodeUtil;
import lombok.Builder;

import java.io.File;
import java.util.Map;

@Builder
public class YatoriUserYingHua extends YatoriUser {


    @Override
    public YatoriLogin toLogin() {

//        AccountCacheYingHua accountCacheYingHua = new AccountCacheYingHua();
//        user.setCache(accountCacheYingHua);
//        //refresh_code:1代表密码错误，
//        Map<String, Object> result = null;
//        do {
//            //获取SESSION
//            String session = null;
//            while ((session = LoginAction.getSESSION(user)) == null) ;
//            accountCacheYingHua.setSession(session);
//            //获取验证码
//            File code = null;
//            while ((code = LoginAction.getCode(user)) == null) ;
//            accountCacheYingHua.setCode(VerificationCodeUtil.aiDiscern(code));
//            FileUtils.deleteFile(code);//删除验证码文件
//            //进行登录操作
//            while ((result = LoginAction.toLogin(user)) == null) ;
//        } while (!(Boolean) result.get("status") && ((String) result.get("msg")).contains("验证码有误"));
//        //对结果进行判定
//        if ((Boolean) result.get("status")) {
//            accountCacheYingHua.setStatus(1);
//            log.info("{}登录成功！", user.getAccount());
//        } else {
//            log.info("{}登录失败，服务器信息>>>{}", user.getAccount(), ((String) result.get("msg")));
//            return null;
//        }
//
//        //为账号维持登录状态-----------------------------------
//        new Thread(() -> {
//            while (true) {
//                Map online;
//                //避免超时
//                while ((online = LoginAction.online(user)) == null) {
//                    try {
//                        Thread.sleep(1000 * 5);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//                //如果含有登录超时字样
//                if (((String) online.get("msg")).contains("更新成功")) {
//                    accountCacheYingHua.setStatus(1);
//                } else if (((String) online.get("msg")).contains("登录超时")) {
//                    accountCacheYingHua.setStatus(2);//设定登录状态为超时
//                    log.info("{}登录超时，正在重新登录...", user.getAccount());
//                    //进行登录
//                    Map<String, Object> map;
//                    do {
//                        //获取验证码
//                        File code = LoginAction.getCode(user);
//                        ((AccountCacheYingHua) user.getCache()).setCode(VerificationCodeUtil.aiDiscern(code));
//                        FileUtils.deleteFile(code);//删除验证码文件
//                        //进行登录操作
//                        map = LoginAction.toLogin(user);
//                    } while (!(Boolean) map.get("status") && ((String) map.get("msg")).contains("验证码有误"));
//                    //对结果进行判定
//                    if ((Boolean) map.get("status")) {
//                        accountCacheYingHua.setStatus(1);
//                        log.info("{}登录成功！", user.getAccount());
//                    } else {
//                        log.info("{}登录失败，服务器信息>>>{}", user.getAccount(), ((String) map.get("msg")));
//                    }
//                }
//                try {
//                    Thread.sleep(1000 * 60);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }).start();
        return null;
    }
}
