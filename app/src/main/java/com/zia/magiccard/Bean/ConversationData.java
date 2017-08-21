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
    private String time;
    private String conversationId;
    private String imageUrl;
    private List<String> members;
    private List<MessageData> messageDatas;

    public List<MessageData> getMessageDatas() {
        return messageDatas;
    }

    public void setMessageDatas(List<MessageData> messageDatas) {
        this.messageDatas = messageDatas;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "conversationId: "+conversationId+"\n"
                +"name: "+name+"\n"
                +"lastContent: "+lastContent+"\n";
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
