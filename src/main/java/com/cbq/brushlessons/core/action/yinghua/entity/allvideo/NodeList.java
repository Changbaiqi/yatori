package com.cbq.brushlessons.core.action.yinghua.entity.allvideo;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
public class NodeList {
    @lombok.Getter(onMethod_ = {@JsonProperty("duration")})
    @lombok.Setter(onMethod_ = {@JsonProperty("duration")})
    private String duration;
    @lombok.Getter(onMethod_ = {@JsonProperty("id")})
    @lombok.Setter(onMethod_ = {@JsonProperty("id")})
    /**
     * nodeId
     */
    private long id;
    @lombok.Getter(onMethod_ = {@JsonProperty("idx")})
    @lombok.Setter(onMethod_ = {@JsonProperty("idx")})
    private long idx;
    @lombok.Getter(onMethod_ = {@JsonProperty("index")})
    @lombok.Setter(onMethod_ = {@JsonProperty("index")})
    private String index;
    @lombok.Getter(onMethod_ = {@JsonProperty("name")})
    @lombok.Setter(onMethod_ = {@JsonProperty("name")})
    private String name;
    @lombok.Getter(onMethod_ = {@JsonProperty("nodeLock")})
    @lombok.Setter(onMethod_ = {@JsonProperty("nodeLock")})
    /**
     * 是否解锁，0代表解锁，1代表未解锁
     */
    private long nodeLock;
    @lombok.Getter(onMethod_ = {@JsonProperty("tabExam")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tabExam")})
    private boolean tabExam;
    @lombok.Getter(onMethod_ = {@JsonProperty("tabFile")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tabFile")})
    private boolean tabFile;
    @lombok.Getter(onMethod_ = {@JsonProperty("tabVideo")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tabVideo")})
    private boolean tabVideo;
    @lombok.Getter(onMethod_ = {@JsonProperty("tabVote")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tabVote")})
    private boolean tabVote;
    @lombok.Getter(onMethod_ = {@JsonProperty("tabWork")})
    @lombok.Setter(onMethod_ = {@JsonProperty("tabWork")})
    private boolean tabWork;
    @lombok.Getter(onMethod_ = {@JsonProperty("unlockTime")})
    @lombok.Setter(onMethod_ = {@JsonProperty("unlockTime")})
    private String unlockTime;
    @lombok.Getter(onMethod_ = {@JsonProperty("unlockTimeStamp")})
    @lombok.Setter(onMethod_ = {@JsonProperty("unlockTimeStamp")})
    private long unlockTimeStamp;
    @lombok.Getter(onMethod_ = {@JsonProperty("videoDuration")})
    @lombok.Setter(onMethod_ = {@JsonProperty("videoDuration")})
    /**
     * 课程总时长
     */
    private String videoDuration;
    @lombok.Getter(onMethod_ = {@JsonProperty("videoState")})
    @lombok.Setter(onMethod_ = {@JsonProperty("videoState")})
    /**
     * 视屏状态，0代表没看过，1代表看了但是没看完，2代表看完了
     */
    private long videoState;
    @lombok.Getter(onMethod_ = {@JsonProperty("voteUrl")})
    @lombok.Setter(onMethod_ = {@JsonProperty("voteUrl")})
    private String voteUrl;
}
