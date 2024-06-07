package com.cbq.yatori.core.action.yinghua.entity.allvideo;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class NodeList {
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("id")
    /**
     * nodeId
     */
    private long id;
    @JsonProperty("idx")
    private long idx;
    @JsonProperty("index")
    private String index;
    @JsonProperty("name")
    private String name;
    @JsonProperty("nodeLock")
    /**
     * 是否解锁，0代表解锁，1代表未解锁,2代表未到解锁时间
     */
    private long nodeLock;
    @JsonProperty("tabExam")
    private boolean tabExam;
    @JsonProperty("tabFile")
    private boolean tabFile;
    @JsonProperty("tabVideo")
    private boolean tabVideo;
    @JsonProperty("tabVote")
    private boolean tabVote;
    @JsonProperty("tabWork")
    private boolean tabWork;
    @JsonProperty("unlockTime")
    private String unlockTime;
    @JsonProperty("unlockTimeStamp")
    private long unlockTimeStamp;
    @JsonProperty("videoDuration")
    /**
     * 课程总时长
     */
    private String videoDuration;
    @JsonProperty("videoState")
    /**
     * 视屏状态，0代表没看过，1代表看了但是没看完，2代表看完了
     */
    private long videoState;
    @JsonProperty("voteUrl")
    private String voteUrl;
}
