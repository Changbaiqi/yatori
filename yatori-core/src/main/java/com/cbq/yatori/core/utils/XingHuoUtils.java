package com.cbq.yatori.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import okhttp3.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class XingHuoUtils extends WebSocketListener {
    public static final String hostUrl = "https://spark-api.xf-yun.com/v3.5/chat";
    public static final String appid = "";
    public static final String apiSecret = "";
    public static final String apiKey = "";

    public static List<RoleContent> historyList=new ArrayList<>(); // 对话历史存储集合

    public static String totalAnswer=""; // 大模型的答案汇总

    // 环境治理的重要性  环保  人口老龄化  我爱我的祖国
    public static  String NewQuestion = "";

    public static final Gson gson = new Gson();

    // 个性化参数
    private String userId;
    private Boolean wsCloseFlag;

    private static Boolean totalFlag=true; // 控制提示用户是否输入
    // 构造函数
    public XingHuoUtils(String userId, Boolean wsCloseFlag) {
        this.userId = userId;
        this.wsCloseFlag = wsCloseFlag;
    }

    // 主函数
    public static void main(String[] args) throws Exception {
        // 个性化参数入口，如果是并发使用，可以在这里模拟
        while (true){
            if(totalFlag){
                Scanner scanner=new Scanner(System.in);
                System.out.print("我：");
                totalFlag=false;
                NewQuestion=scanner.nextLine();
                // 构建鉴权url
                String authUrl = getAuthUrl(hostUrl, apiKey, apiSecret);
                OkHttpClient client = new OkHttpClient.Builder().build();
                String url = authUrl.toString().replace("http://", "ws://").replace("https://", "wss://");
                Request request = new Request.Builder().url(url).build();
                for (int i = 0; i < 1; i++) {
                    totalAnswer="";
                    WebSocket webSocket = client.newWebSocket(request, new XingHuoUtils(i + "",
                            false));
                }
            }else{
                Thread.sleep(200);
            }
        }
    }

    public static boolean canAddHistory(){  // 由于历史记录最大上线1.2W左右，需要判断是能能加入历史
        int history_length=0;
        for(RoleContent temp:historyList){
            history_length=history_length+temp.content.length();
        }
        if(history_length>12000){
            historyList.remove(0);
            historyList.remove(1);
            historyList.remove(2);
            historyList.remove(3);
            historyList.remove(4);
            return false;
        }else{
            return true;
        }
    }

    // 线程来发送音频与参数
    class MyThread extends Thread {
        private WebSocket webSocket;

        public MyThread(WebSocket webSocket) {
            this.webSocket = webSocket;
        }

        public void run() {
            try {
                JSONObject requestJson=new JSONObject();

                JSONObject header=new JSONObject();  // header参数
                header.put("app_id",appid);
                header.put("uid", UUID.randomUUID().toString().substring(0, 10));

                JSONObject parameter=new JSONObject(); // parameter参数
                JSONObject chat=new JSONObject();
                chat.put("domain","generalv2");
                chat.put("temperature",0.5);
                chat.put("max_tokens",4096);
                parameter.put("chat",chat);

                JSONObject payload=new JSONObject(); // payload参数
                JSONObject message=new JSONObject();
                JSONArray text=new JSONArray();

                // 历史问题获取
                if(historyList.size()>0){
                    for(RoleContent tempRoleContent:historyList){
                        text.add(JSON.toJSON(tempRoleContent));
                    }
                }

                // 最新问题
                RoleContent roleContent=new RoleContent();
                roleContent.role="user";
                roleContent.content=NewQuestion;
                text.add(JSON.toJSON(roleContent));
                historyList.add(roleContent);


                message.put("text",text);
                payload.put("message",message);


                requestJson.put("header",header);
                requestJson.put("parameter",parameter);
                requestJson.put("payload",payload);
                // System.err.println(requestJson); // 可以打印看每次的传参明细
                webSocket.send(requestJson.toString());
                // 等待服务端返回完毕后关闭
                while (true) {
                    // System.err.println(wsCloseFlag + "---");
                    Thread.sleep(200);
                    if (wsCloseFlag) {
                        break;
                    }
                }
                webSocket.close(1000, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        System.out.print("大模型：");
        MyThread myThread = new MyThread(webSocket);
        myThread.start();
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        // System.out.println(userId + "用来区分那个用户的结果" + text);
        JsonParse myJsonParse = gson.fromJson(text, JsonParse.class);
        if (myJsonParse.header.code != 0) {
            System.out.println("发生错误，错误码为：" + myJsonParse.header.code);
            System.out.println("本次请求的sid为：" + myJsonParse.header.sid);
            webSocket.close(1000, "");
        }
        List<Text> textList = myJsonParse.payload.choices.text;
        for (Text temp : textList) {
            System.out.print(temp.content);
            totalAnswer=totalAnswer+temp.content;
        }
        if (myJsonParse.header.status == 2) {
            // 可以关闭连接，释放资源
            System.out.println();
            System.out.println("*************************************************************************************");
            if(canAddHistory()){
                RoleContent roleContent=new RoleContent();
                roleContent.setRole("assistant");
                roleContent.setContent(totalAnswer);
                historyList.add(roleContent);
            }else{
                historyList.remove(0);
                RoleContent roleContent=new RoleContent();
                roleContent.setRole("assistant");
                roleContent.setContent(totalAnswer);
                historyList.add(roleContent);
            }
            wsCloseFlag = true;
            totalFlag=true;
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        try {
            if (null != response) {
                int code = response.code();
                System.out.println("onFailure code:" + code);
                System.out.println("onFailure body:" + response.body().string());
                if (101 != code) {
                    System.out.println("connection failed");
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    // 鉴权方法
    public static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        // 时间
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        // 拼接
        String preStr = "host: " + url.getHost() + "\n" +
                "date: " + date + "\n" +
                "GET " + url.getPath() + " HTTP/1.1";
        // System.err.println(preStr);
        // SHA256加密
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "hmacsha256");
        mac.init(spec);

        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        // Base64加密
        String sha = Base64.getEncoder().encodeToString(hexDigits);
        // System.err.println(sha);
        // 拼接
        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"", apiKey, "hmac-sha256", "host date request-line", sha);
        // 拼接地址
        HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("https://" + url.getHost() + url.getPath())).newBuilder().//
                addQueryParameter("authorization", Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8))).//
                addQueryParameter("date", date).//
                addQueryParameter("host", url.getHost()).//
                build();

        // System.err.println(httpUrl.toString());
        return httpUrl.toString();
    }

    //返回的json结果拆解
    class JsonParse {
        Header header;
        Payload payload;
    }

    class Header {
        int code;
        int status;
        String sid;
    }

    class Payload {
        Choices choices;
    }

    class Choices {
        List<Text> text;
    }

    class Text {
        String role;
        String content;
    }
    class RoleContent{
        String role;
        String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
