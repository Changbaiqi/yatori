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
public class YingHuaOnlineOoperation extends YingHuaAbstractLogin implements Runnable{
    private User user;
    @Override
    public boolean action(YingHuaLogin loginAction, User user, Object o) {
        this.user = user;
        new Thread(this).start(); //启动登录保持器
        return true;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        AccountCacheYingHua accountCacheYingHua = new AccountCacheYingHua();
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
                log.info("保持登录");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
