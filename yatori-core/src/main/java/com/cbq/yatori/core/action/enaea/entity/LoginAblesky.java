package com.cbq.yatori.core.action.enaea.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginAblesky {
    private String curMillis; //时间戳

    @JsonProperty("sS")
    private String sS; //101代表账号或者密码错误

    @JsonProperty("iI")
    private Boolean iI;

    @JsonProperty("uI")
    private Long uI;
    @JsonProperty("isBinded")
    private Boolean isBinded;
    @JsonProperty("success")
    private Boolean success; //是否登录成功

    @JsonProperty("iP")
    private Boolean iP;

    @JsonProperty("uN")
    private String uN; //用户名

    @JsonProperty("alertMessage")
    private String alertMessage;//提示信息

    @JsonProperty("wrongTimes")
    private Integer wrongTimes; // 密码错误次数

    @JsonProperty("tryTimesLeft")
    private Integer tryTimesLeft; //输入密码错误剩余尝试次数

    /**
     * 转成对象
     * @param ky
     * @return
     */
    public static LoginAblesky toLoginAblesky(String ky){
        Pattern  pattern = Pattern.compile("ablesky_([^\\(]+)\\(([^\\)]+)\\);");
        Matcher matcher = pattern.matcher(ky);
        if(matcher.find()){
            String curMillis = matcher.group(1);
            String jsonStr=matcher.group(2);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                LoginAblesky loginAblesky = objectMapper.readValue(jsonStr,LoginAblesky.class);
                loginAblesky.setCurMillis(curMillis);
                return loginAblesky;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }
        return null;
    }

}
