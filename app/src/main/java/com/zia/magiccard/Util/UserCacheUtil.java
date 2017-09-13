package com.zia.magiccard.Util;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.zia.magiccard.Bean.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-23.
 */

public class UserCacheUtil {

    private static UserCacheUtil instance;
    private List<UserData> userDatas;
    private static final String TAG = "UserCacheUtilTest";

    public static UserCacheUtil getInstance(){
        if(instance == null){
            synchronized (UserCacheUtil.class){
                if(instance == null){
                    instance = new UserCacheUtil();
                }
            }
        }
        return instance;
    }

    private UserCacheUtil(){
        userDatas = new ArrayList<>();
    }

    public interface OnUserDataGet{
        void onUserFind(UserData userData);
    }

    public void getUserDataAsyncByMember(List<String> members,OnUserDataGet onUserDataGet){
//        if(members.size() < 2){
//            return;
//        }else Log.d(TAG,"members.size() < 2");
        if(members.size() == 0){
            Log.d(TAG,"error:  members.size() == 0");
            return;
        }
        if(members.size() < 2) Log.d(TAG,"members.size() < 2");
        for(int i = 0; i< members.size(); i++){
            if(!members.get(i).equals(AVUser.getCurrentUser().getObjectId())){
                getUserDataAsyncById(members.get(i),onUserDataGet);
                break;
            }
        }
    }

    public void getUserDataAsyncById(String userId, final OnUserDataGet onUserDataGet){
        for (UserData userData : userDatas){
            if(userData.getObjectId().equals(userId)){
                onUserDataGet.onUserFind(userData);
                return;
            }
        }
        getUserDataAsync(userId,onUserDataGet);
    }

    /**
     * 最底层，新线程获取userdata
     * @param userId
     * @param onUserDataGet
     */
    public void getUserDataAsync(String userId, final OnUserDataGet onUserDataGet){
        AVQuery<AVUser> query = new AVQuery<AVUser>("_User");
        query.getInBackground(userId, new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e == null){
                    UserData userData = new UserData();
                    userData.setIntroduce(avUser.getString("introduce"));
                    userData.setIsboy(avUser.getBoolean("isboy"));
                    userData.setNickname(avUser.getString("nickname"));
                    userData.setInstallationId(avUser.getString("installationId"));
                    userData.setObjectId(avUser.getObjectId());
                    userData.setUsername(avUser.getUsername());
                    AVFile file = avUser.getAVFile("head");
                    if(file != null){
                        userData.setHeadUrl(file.getUrl());
                    }
                    userDatas.add(userData);
                    onUserDataGet.onUserFind(userData);
                }
            }
        });

    }

    public UserData getUserDataById(String userId) throws AVException {
        for (UserData userData : userDatas){
            if(userData.getObjectId().equals(userId)){
                return userData;
            }
        }
        return getUserData(userId);
    }

    private UserData getUserData(String userId) throws AVException {
        AVQuery<AVUser> query = new AVQuery<AVUser>("_User");
        AVUser avUser = query.get(userId);
        UserData userData = new UserData();
        userData.setIntroduce(avUser.getString("introduce"));
        userData.setIsboy(avUser.getBoolean("isboy"));
        userData.setNickname(avUser.getString("nickname"));
        userData.setInstallationId(avUser.getString("installationId"));
        userData.setObjectId(avUser.getObjectId());
        userData.setUsername(avUser.getUsername());
        AVFile file = avUser.getAVFile("head");
        if(file != null){
            userData.setHeadUrl(file.getUrl());
        }
        userDatas.add(userData);
        return userData;
    }

}
