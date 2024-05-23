package com.cbq.yatori.core.aggregate.yinghua.login;

import com.cbq.yatori.core.action.yinghua.LoginAction;
import com.cbq.yatori.core.aggregate.YatoriLogin;
import com.cbq.yatori.core.aggregate.yinghua.YingHuaUser;
import com.cbq.yatori.core.entity.AccountCacheYingHua;
import com.cbq.yatori.core.entity.User;
import com.cbq.yatori.core.utils.FileUtils;
import com.cbq.yatori.core.utils.VerificationCodeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class YingHuaLogin extends YatoriLogin {
    private ArrayList<YingHuaAbstractLogin> chains = new ArrayList<>();
    private User user;



    @Override
    public void action(User user){
        Map<String, Object> stringObjectMap = loginAction();

        //责任链
        chains.forEach(yingHuaAbstractLogin ->{if(!yingHuaAbstractLogin.action(this,user, stringObjectMap)) return;});
    }

    public YingHuaLogin(User user) {
        this.user = user;
    }

    public Map<String,Object> loginAction(){
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


    public void addOperation(YingHuaAbstractLogin yingHuaAbstractLogin){
        chains.add(yingHuaAbstractLogin);
    }

    public YingHuaUser toLogin(){
        addOperation(new YingHuaCodeOperation()); //添加验证码判别策略
        addOperation(new YingHuaOnlineOoperation()); //保持登录状态
        action(user);
        return new YingHuaUser();
    }

    public static YingHuaLogin loginAction(User user) {
        return new YingHuaLogin(user);
    }

}
