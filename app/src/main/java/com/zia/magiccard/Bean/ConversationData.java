package com.zia.magiccard.Bean;

import com.avos.avoscloud.AVObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zia on 17-8-19.
 */

public class ConversationData implements Serializable {
    private String name;
    private String lastContent;
    private long time;
    private String conversationId;
    private String imageUrl;
    private List<String> members;

    @Override
    public String toString() {
        return "conversationId: "+conversationId+"\n"
                +"name: "+name+"\n"
                +"lastContent: "+lastContent+"\n"
                +"imageUrl: "+imageUrl+"\n"
                +"time: "+time+"\n";
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastContent() {
        return lastContent;
    }

    public void setLastContent(String lastContent) {
        this.lastContent = lastContent;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
