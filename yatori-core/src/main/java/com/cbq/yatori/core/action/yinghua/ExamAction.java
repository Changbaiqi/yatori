package com.cbq.yatori.core.action.yinghua;

import com.cbq.yatori.core.action.yinghua.entity.examinform.ConverterExamInform;
import com.cbq.yatori.core.action.yinghua.entity.examinform.ExamInformRequest;
import com.cbq.yatori.core.action.yinghua.entity.examinform.ExamInformResult;
import com.cbq.yatori.core.action.yinghua.entity.examstart.ConverterStartExam;
import com.cbq.yatori.core.action.yinghua.entity.examstart.StartExamRequest;
import com.cbq.yatori.core.action.yinghua.entity.examtopic.ExamTopic;
import com.cbq.yatori.core.action.yinghua.entity.examtopic.ExamTopics;
import com.cbq.yatori.core.action.yinghua.entity.examtopic.TopicSelect;
import com.cbq.yatori.core.entity.AccountCacheYingHua;
import com.cbq.yatori.core.entity.AiType;
import com.cbq.yatori.core.entity.User;
import com.cbq.yatori.core.utils.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 长白崎
 * @version 1.0
 * @description: TODO 考试相关
 * @date 2023/12/1 12:11
 */
@Slf4j
public class ExamAction {

    /**
     * 题目转换
     *
     * @param examHtml HTML文本
     * @return 返回包装类
     */
    private static ExamTopics turnExamTopic(String examHtml) {
        ExamTopics exchangeTopics = new ExamTopics();
        exchangeTopics.setExamTopics(new LinkedHashMap<String, ExamTopic>());
        Pattern pattern = Pattern.compile("<li>[ \\f\\n\\r\\t\\v]*<a data-id=\"([^\"]*)\"[ \\f\\n\\r\\t\\v]*href=\"[^\"]*\"[ \\f\\n\\r\\t\\v]*class=\"[^\"]*\"[ \\f\\n\\r\\t\\v]*id=\"[^\"]*\"[ \\f\\n\\r\\t\\v]*data-index=\"[^\"]*\"[ \\f\\n\\r\\t\\v]*onclick=\"[^\"]*\">([^<]*)</a>[ \\f\\n\\r\\t\\v]*</li>");
        Matcher matcher = pattern.matcher(examHtml);
        HashMap<String, String> topicMap = new HashMap<>();
        while (matcher.find()) {
            String answerId = matcher.group(1);
            String index = matcher.group(2);
            topicMap.put(index, answerId);
//            System.out.println("answerId："+answerId+"  题目序号："+index);
        }

        pattern = Pattern.compile("<form method=\"post\" action=\"/api/[^/]*/submit\">([\\w\\W]*?)</form>");
        matcher = pattern.matcher(examHtml);
        while (matcher.find()) {
            String topicHtml = matcher.group(1);
            Pattern topicNumPattern = Pattern.compile("<span class=\"num\">[\\D]*?([\\d]+)");
            Matcher topicNumMatcher = topicNumPattern.matcher(topicHtml);

            String num = null; // 题目号码
            String tag = null; // 题目类型
            String source = null;
            String content = null;
            List<TopicSelect> selects = new ArrayList<>();

            if (topicNumMatcher.find()) {
                num = topicNumMatcher.group(1); // 题目号码
//                System.out.println("题目号码：" + num);
            }
            Pattern topicTag = Pattern.compile("<span class=\"tag\">([\\s\\S]*?)</span>");
            Matcher topicTagMatcher = topicTag.matcher(topicHtml);
            if (topicTagMatcher.find()) {
                tag = topicTagMatcher.group(1); // 题目类型
//                System.out.println("题目类型：" + tag);
            }

            Pattern topicSource = Pattern.compile("<span[ \\f\\n\\r\\t\\v]*class=\"txt\">[(]*[<span>]*([\\d]*)[</span>]*分[)]*</span>");
            Matcher topicSourceMatcher = topicSource.matcher(topicHtml);
            if (topicSourceMatcher.find()) {
                source = topicSourceMatcher.group(1); // 题目分数
//                System.out.println("题目分数：" + source);
            }

            if (tag != null && (tag.equals("单选") || tag.equals("多选") || tag.equals("判断"))) {
                Pattern topicContent = Pattern.compile("<div[ \\f\\n\\r\\t\\v]*class=\"content\"[ \\f\\n\\r\\t\\v]*style=\"[^\"]*\">([\\s\\S]*?)</div>");
                Matcher topicContentMatcher = topicContent.matcher(topicHtml);
                if (topicContentMatcher.find()) {
                    content = topicContentMatcher.group(1); // 题目内容
//                System.out.println("题目内容：" + content);
                }

                Pattern topicSelectPattern = Pattern.compile("<li>[^<]*<label>[^<]*<input type=\"([^\"]*)\"[^v]*value=\"([^\"]*)\"[ \\f\\n\\r\\t\\v]*[checked=\"checked\"]*[ \\f\\n\\r\\t\\v]*class=\"[^\"]*\"[ \\f\\n\\r\\t\\v]*name=\"[^\"]*\">[ \\f\\n\\r\\t\\v]*<span class=\"num\">([^<]*)</span>[ \\f\\n\\r\\t\\v]*<span[ \\f\\n\\r\\t\\v]*class=\"txt\">([^<]*)</span>[ \\f\\n\\r\\t\\v]*</label>[ \\f\\n\\r\\t\\v]*</li>");
                Matcher topicSelectMatcher = topicSelectPattern.matcher(topicHtml);
                while (topicSelectMatcher.find()) {

                    String selectType = topicSelectMatcher.group(1); // 题目类型
                    String selectValue = topicSelectMatcher.group(2); // 选项值
                    String selectNum = topicSelectMatcher.group(3);
                    String selectText = topicSelectMatcher.group(4); // 选项文本
//                System.out.println(selectValue + "-----" + selectText);
                    selects.add(new TopicSelect(selectValue, selectNum, selectText));
                }

                // 过滤掉所有违法字符串
                content = content.replace("<p>", "").replace("</p>", "\n").replace("&nbsp;", "");
            }
            // 填空题采用策略
            if (tag.equals("填空")) {
                // 匹配题目内容
                Pattern topicContent = Pattern.compile("<div[ \\f\\n\\r\\t\\v]*class=\"content\"[ \\f\\n\\r\\t\\v]*style=\"[^\"]*\">([\\s\\S]*?)</div>");
                Matcher topicContentMatcher = topicContent.matcher(topicHtml);
                if (topicContentMatcher.find()) {
                    content = topicContentMatcher.group(1); // 题目内容
//                System.out.println("题目内容：" + content);
                }
                // 这里是先正则添加填空项
                Pattern topicSelectPattern = Pattern.compile("<input ((?<!answer).)+answer_(\\d)+((?<!>).)+>");
                Matcher topicSelectMatcher = topicSelectPattern.matcher(topicHtml);
                while (topicSelectMatcher.find()) {
                    String answerId = topicSelectMatcher.group(2);
                    selects.add(new TopicSelect(answerId, answerId, ""));

                }
                // 替换代码填空项
                Pattern topicClearCodeSelectPattern = Pattern.compile("<code>((?<!answer).)+answer_(\\d)+((?<!</code>).)+</code>");
                Matcher topicClearCodeSelectMatcher = null;
                if (content != null) {
                    topicClearCodeSelectMatcher = topicClearCodeSelectPattern.matcher(content);
                }
                if (topicClearCodeSelectMatcher != null) {
                    while (topicClearCodeSelectMatcher.find()) {
                        String answerId = topicClearCodeSelectMatcher.group(2);
                        content = content.replace(topicClearCodeSelectMatcher.group(), "（answer_" + answerId + "）");
                    }
                }
                // 替换普通填空项
                Pattern topicClearSelectPattern = Pattern.compile("<input ((?<!answer).)+answer_(\\d)+((?<!>).)+>");
                Matcher topicClearSelectMatcher = null;
                if (content != null) {
                    topicClearSelectMatcher = topicClearSelectPattern.matcher(content);
                }
                if (topicClearSelectMatcher != null) {
                    while (topicClearSelectMatcher.find()) {
                        String answerId = topicClearSelectMatcher.group(2);
                        content = content.replace(topicClearSelectMatcher.group(), "（answer_" + answerId + "）");
                    }
                }
                // 过滤掉所有违法字符串
                if (content != null) {
                    content = content.replace("<p>", "").replace("</p>", "\n").replace("&nbsp;", "");
                }
            }

            ExamTopic examTopic = new ExamTopic();
            examTopic.setAnswerId(topicMap.get(num)); // 设置回复Id
            examTopic.setIndex(num); // 设置题目号码
            examTopic.setSource(source); // 设置题目分数
            examTopic.setContent(content); // 设置题目内容
            examTopic.setType(tag); // 设置题目类型
            examTopic.setSelects(selects); // 设置题目选项
            exchangeTopics.getExamTopics().put(topicMap.get(num), examTopic);
        }

        return exchangeTopics;
    }

    /**
     * 获取考试相关的一些信息
     * {"_code":0,"status":true,"msg":"获取数据成功","result":{"list":[{"id":1006680,"title":"0-3岁婴幼儿家庭教育与指导","topicNumber":33,"score":100,"addTime":"2024-04-09 12:10:58","nodeId":1430751,"courseId":1010822,"limitedTime":120,"remarks":"","startTime":"2024-04-09 12:10:10","endTime":"2024-04-30 23:55:55","createUserId":"1249678","classList":"[]","isPrivate":"0","teacherType":"1","allow":"1","frequency":"1","hasCollect":"0","schoolId":"9","parsing":"0","addDate":"2024-04-09","random":"0","randData":null,"randNumber":"0","type":3,"flag":0,"start":1,"finish":0,"url":"https:\/\/mooc.lidapoly.edu.cn\/api\/exam?nodeId=1430751&examId=1006680&token=sid.8bXX72LTPHQNhcki2CJf9cZ3oINhqt"}]}}
     * 这里会获取到考试内容相关的信息
     * 其中list中的id其实就是examId,nodeId就是nodeId
     *
     * @param user   用户类
     * @param nodeId 对应考试的节点id
     */
    public static ExamInformRequest getExam(User user, String nodeId) {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------393670526422134055864302");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform", "Android")
                .addFormDataPart("version", "1.4.8")
                .addFormDataPart("nodeId", nodeId)
                .addFormDataPart("token", cache.getToken())
                .addFormDataPart("terminal", "Android")
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/node/exam.json")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------393670526422134055864302")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                return ConverterExamInform.fromJsonString(response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 开始考试接口
     *
     * @param user     用户
     * @param courseId 课程ID
     * @param nodeId   对应章节结点Id
     * @param examId   考试Id
     */
    public static StartExamRequest startExam(User user, String courseId, String nodeId, String examId) {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------244120585315146738764763");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform", "Android")
                .addFormDataPart("version", "1.4.8")
                .addFormDataPart("nodeId", nodeId)
                .addFormDataPart("token", cache.getToken())
                .addFormDataPart("terminal", "Android")
                .addFormDataPart("examId", examId)
                .addFormDataPart("courseId", courseId)
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/exam/start.json")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------244120585315146738764763")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                return ConverterStartExam.fromJsonString(response.body().string());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 获取所有考试题目
     *
     * @param user   用户信息
     * @param nodeId 节点Id
     * @param examId 考试Id
     * @return
     */
    public static ExamTopics getExamTopic(User user, String nodeId, String examId) {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, "{}");
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/exam.json?nodeId=" + nodeId + "&examId=" + examId + "&token=" + cache.getToken())
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            ExamTopics examTopics = null;
            if (response.body() != null) {
                examTopics = turnExamTopic(response.body().string());
            }
            return examTopics;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param examTopic 题目
     * @return 返回答案字符串
     */
    public static String aiAnswerFormChatGLM(AiType aiType, String API_KEY, ExamTopic examTopic, String courseTitle) {
        StringBuilder problem = new StringBuilder();

        problem.append("题目类型：\n").append(examTopic.getType()).append("\n");
        problem.append("题目相关课程信息：\n");
        problem.append(courseTitle).append("\n");
        problem.append("题目内容：\n").append(examTopic.getContent()).append("\n");


        if (examTopic.getType().equals("单选") || examTopic.getType().equals("多选") || examTopic.getType().equals("判断")) {
            for (TopicSelect select : examTopic.getSelects()) {
                problem.append(select.getValue()).append("-----").append(select.getTxt()).append("\n");
            }

            problem.append("提问：\n");
            problem.append("这题的答案是什么？");
            problem.append("回答要求限制：\n");
            problem.append("注意你只需要回答选项字母，不能回答任何选项字母无关的任何内容，包括解释以及标点符也不需要。就算你不知道选什么也随机选输出A或B。");
        }
        if (examTopic.getType().equals("填空")) {
            problem.append("提问：\n");
            problem.append("这题的答案是什么？");
            problem.append("回答要求限制：\n");
            problem.append("其中，“（answer_数字）”相关字样的地方是你需要填写答案的地方，现在你只需要回复我对应每个填空项的答案即可，并且采用json格式的回复方式，比如{\"answer_1\":\"答案\",\"answer_2\":\"答案\"}，其中“answer_数字”字样与对应填空项中的答案对应，其他不符合json格式的内容无需回复。你只需回复答案对应json，无需回答任何解释！！！");
        }
//        System.out.println(problem.toString());
//        String chatMessage = ChatGLMUtil.getChatMessage(API_KEY, API_SECRET, problem.toString());
        ChatGLMChat chatGLMChat = new ChatGLMChat().builder()
                .addMessage(ChatGLMMessage.builder().role("user").content(problem.toString()).build())
                .build();
        String chatMessage=null;
        switch (aiType){
            case CHATGLM -> {
                chatMessage = ChatGLMUtil.getChatMessage(API_KEY, chatGLMChat);
            }
            case XINGHUO -> {
                chatMessage = XHAIUtil.getChatMessage(API_KEY, chatGLMChat);
            }
            case TONGYI -> {
                chatMessage = TongYiUtil.getChatMessage(API_KEY, chatGLMChat);
            }
        }


        try {
            if (examTopic.getType().equals("填空")) {

                ObjectMapper objectMapper = new ObjectMapper();
                HashMap<String, String> json = objectMapper.readValue(chatMessage, HashMap.class);
                examTopic.getSelects().forEach((select) -> {
                    String answer = json.get("answer_" + select.getNum());
                    select.setTxt(answer);
                });
            }
        } catch (Exception ignored) {

        }
        return chatMessage;
    }


    /**
     * 用于检测AI是否可用
     *
     * @param API_KEY
     * @return
     */
    public static boolean checkChatGLM(AiType aiType,String API_KEY) {
        ChatGLMChat chatGLMChat = new ChatGLMChat().builder()
                .addMessage(ChatGLMMessage.builder().role("user").content("Hello World!").build())
                .build();
        String chatMessage = null;
        switch (aiType){
            case CHATGLM -> {
                chatMessage = ChatGLMUtil.getChatMessage(API_KEY, chatGLMChat);
            }
            case XINGHUO -> {
                chatMessage = XHAIUtil.getChatMessage(API_KEY, chatGLMChat);
            }
            case TONGYI -> {
                chatMessage = TongYiUtil.getChatMessage(API_KEY, chatGLMChat);
            }
        }
        if (chatMessage.isEmpty()) return false;
        return true;
    }

    /**
     * 提交答题的接口
     *
     * @param user     用户
     * @param examId   答题的考试试卷Id
     * @param answerId 答题的题目Id
     * @param answer   所提交的答案或选项，比如A，B等
     * @param finish   是否是最后提交并且结束考试，0代表不是，1代表是
     */
    public static void submitExam(User user, String examId, String answerId, String answer, String finish) {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------326388482122783598484776");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("platform", "Android")
                .addFormDataPart("version", "1.4.8")
                .addFormDataPart("examId", examId)
                .addFormDataPart("terminal", "Android")
                .addFormDataPart("answerId", answerId)
                .addFormDataPart("finish", finish)
                .addFormDataPart("token", cache.getToken());
        if (answer.length() == 1) {
            builder.addFormDataPart("answer", answer.charAt(0) + "");
        } else {
            for (int i = 0; i < answer.length(); ++i) {
                builder.addFormDataPart("answer[]", answer.charAt(i) + "");
            }
        }
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/exam/submit.json")
                .method("POST", builder.build())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------326388482122783598484776")
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void submitExam(User user, String examId, String answerId, List<TopicSelect> topicSelects, String finish) {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------326388482122783598484776");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("platform", "Android")
                .addFormDataPart("version", "1.4.8")
                .addFormDataPart("examId", examId)
                .addFormDataPart("terminal", "Android")
                .addFormDataPart("answerId", answerId)
                .addFormDataPart("finish", finish)
                .addFormDataPart("token", cache.getToken());
        topicSelects.forEach((select) -> {
            builder.addFormDataPart("answer_" + select.getNum(), select.getTxt());
        });
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/exam/submit.json")
                .method("POST", builder.build())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------326388482122783598484776")
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取考试相关的一些信息
     * {"_code":0,"status":true,"msg":"获取数据成功","result":{"list":[{"id":1006680,"title":"0-3岁婴幼儿家庭教育与指导","topicNumber":33,"score":100,"addTime":"2024-04-09 12:10:58","nodeId":1430751,"courseId":1010822,"limitedTime":120,"remarks":"","startTime":"2024-04-09 12:10:10","endTime":"2024-04-30 23:55:55","createUserId":"1249678","classList":"[]","isPrivate":"0","teacherType":"1","allow":"1","frequency":"1","hasCollect":"0","schoolId":"9","parsing":"0","addDate":"2024-04-09","random":"0","randData":null,"randNumber":"0","type":3,"flag":0,"start":1,"finish":0,"url":"https:\/\/mooc.lidapoly.edu.cn\/api\/exam?nodeId=1430751&examId=1006680&token=sid.8bXX72LTPHQNhcki2CJf9cZ3oINhqt"}]}}
     * 这里会获取到考试内容相关的信息
     * 其中list中的id其实就是examId,nodeId就是nodeId
     *
     * @param user
     * @param nodeId
     * @return
     */
    public static ExamInformRequest getWork(User user, String nodeId) {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------393670526422134055864302");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("platform", "Android")
                .addFormDataPart("version", "1.4.8")
                .addFormDataPart("nodeId", nodeId)
                .addFormDataPart("token", cache.getToken())
                .addFormDataPart("terminal", "Android")
                .build();
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/node/work.json")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------393670526422134055864302")
                .build();
        try {
            Response response = client.newCall(request).execute();
            ExamInformRequest examInformRequest = null;
            if (response.body() != null) {
                examInformRequest = ConverterExamInform.fromJsonString(response.body().string());
            }
            response.close();
            return examInformRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 开始写作业接口
     *
     * @param user     用户
     * @param courseId 课程ID
     * @param nodeId   对应章节结点Id
     * @param workId   作业Id
     */
    public static StartExamRequest startWork(User user, String courseId, String nodeId, String workId) {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/work/start.json?nodeId=" + nodeId + "&workId=" + workId + "&courseId=" + courseId + "&token=" + cache.getToken())
                .method("GET", null)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();
            StartExamRequest startExamRequest = null;
            if (response.body() != null) {
                startExamRequest = ConverterStartExam.fromJsonString(response.body().string());
            }
            response.close();
            return startExamRequest;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取所有作业题目
     *
     * @param user   用户信息
     * @param nodeId 节点Id
     * @param workId 考试Id
     * @return
     */
    public static ExamTopics getWorkTopic(User user, String nodeId, String workId) {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, "{}");
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/work?nodeId=" + nodeId + "&workId=" + workId + "&token=" + cache.getToken())
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .build();
        try {
            Response response = client.newCall(request).execute();

            ExamTopics examTopics = null;
            if (response.body() != null) {
                examTopics = turnExamTopic(response.body().string());
            }
            response.close();
            return examTopics;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 提交作业答题的接口
     *
     * @param user     用户
     * @param workId   答题的作业试卷Id
     * @param answerId 答题的题目Id
     * @param answer   所提交的答案或选项，比如A，B等
     * @param finish   是否是最后提交并且结束考试，0代表不是，1代表是
     */
    public static void submitWork(User user, String workId, String answerId, String answer, String finish) {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------326388482122783598484776");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("platform", "Android")
                .addFormDataPart("version", "1.4.8")
                .addFormDataPart("workId", workId)
                .addFormDataPart("terminal", "Android")
                .addFormDataPart("answerId", answerId)
                .addFormDataPart("finish", finish)
                .addFormDataPart("token", cache.getToken());
        if (answer.length() == 1) {
            builder.addFormDataPart("answer", answer.charAt(0) + "");
        } else {
            for (int i = 0; i < answer.length(); ++i) {
                builder.addFormDataPart("answer[]", answer.charAt(i) + "");
            }
        }
        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/work/submit.json")
                .method("POST", builder.build())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------326388482122783598484776")
                .build();
        try {
            Response response = client.newCall(request).execute();
            response.close();
//            log.info(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 提交作业答题的接口
     *
     * @param user     用户
     * @param workId   答题的作业试卷Id
     * @param answerId 答题的题目Id
     * @param finish   是否是最后提交并且结束考试，0代表不是，1代表是
     */
    public static void submitWork(User user, String workId, String answerId, List<TopicSelect> topicSelects, String finish) {
        AccountCacheYingHua cache = (AccountCacheYingHua) user.getCache();
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)// 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .sslSocketFactory(CustomTrustManager.getSSLContext().getSocketFactory(), new CustomTrustManager())
                .hostnameVerifier((hostname, session) -> true) // Bypass hostname verification
                .build();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------326388482122783598484776");
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("platform", "Android")
                .addFormDataPart("version", "1.4.8")
                .addFormDataPart("workId", workId)
                .addFormDataPart("terminal", "Android")
                .addFormDataPart("answerId", answerId)
                .addFormDataPart("finish", finish)
                .addFormDataPart("token", cache.getToken());
        topicSelects.forEach((select) -> {
            builder.addFormDataPart("answer_" + select.getNum(), select.getTxt());
        });

        Request request = new Request.Builder()
                .url(user.getUrl() + "/api/work/submit.json")
                .method("POST", builder.build())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", user.getUrl().replace("https://", "").replace("http://", "").replace("/", ""))
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------326388482122783598484776")
                .build();
        try {
            Response response = client.newCall(request).execute();
//            log.info(response.body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
