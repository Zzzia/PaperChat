package com.zia.magiccard.Bean;

import java.io.Serializable;

/**
 * Created by zia on 17-8-20.
 */

public class MessageData implements Serializable {

    private int type;//消息类型
    private long time;//时间
    private String content;//文本或链接
    private String userId;//objectId
    private String nickname;//nickname
    private String headUrl;
    private String audioUrl;

    @Override
    public String toString() {
        return "type: "+type+"\n"+
                "content: "+ content+"\n"+
                "userId: "+userId+"\n"
                +"nickname : "+nickname+"\n"
                +"headUrl :"+headUrl+"\n"
                +"time :"+time+"\n";
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
