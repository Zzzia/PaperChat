package com.zia.magiccard.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zia on 17-8-21.
 * 好友分组
 */

public class ClassifyData implements Serializable {

    private String className;
    private List<UserData> userDatas;

    @Override
    public String toString() {
        return "好友分组信息: \n"+
                "className: "+className+"\n"
                +"users: "+userDatas.toString()+"\n";
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<UserData> getUserDatas() {
        return userDatas;
    }

    public void setUserDatas(List<UserData> userDatas) {
        this.userDatas = userDatas;
    }
}
