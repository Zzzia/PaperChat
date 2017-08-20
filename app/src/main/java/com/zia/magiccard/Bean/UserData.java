package com.zia.magiccard.Bean;

import java.io.Serializable;

/**
 * Created by zia on 17-8-19.
 */

public class UserData implements Serializable {

    private String objectId;
    private boolean isboy;
    private String installationId;
    private String nickname;
    private String username;
    private String headUrl;
    private String introduce;

    @Override
    public String toString() {
        return "objectId: "+objectId+"\n"
                +"isboy: "+isboy+"\n"
                +"installationId: "+installationId+"\n"
                +"headUrl: "+headUrl+"\n"
                +"username: "+username+"\n"
                +"introduce: "+introduce+"\n"
                +"nickname: "+nickname+"\n";
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public boolean isboy() {
        return isboy;
    }

    public void setIsboy(boolean isboy) {
        this.isboy = isboy;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
