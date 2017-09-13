package com.zia.magiccard.Util;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.zia.magiccard.Bean.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-19.
 */

public class UserUtil {

    private static final String TAG = "UserModelTest";

    public interface OnUserGetListener{
        void getUserList(List<UserData> userDataList);
    }

    public static void SearchUserByNickName(final String nickname, final OnUserGetListener listener) {
        AVQuery<AVUser> userQuery = new AVQuery<>("_User");
        userQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                List<UserData> userDataList = new ArrayList<>();
                if(list == null) {
                    Log.e(TAG,"list == null");
                    return;
                }
                for (AVUser object: list){
                    if(object.getString("nickname").contains(nickname)){
                        UserData userData = new UserData();
                        userData.setNickname(object.getString("nickname"));
                        userData.setInstallationId(object.getString("installationId"));
                        userData.setUsername(object.getString("username"));
                        userData.setObjectId(object.getObjectId());
                        userData.setIsboy(object.getBoolean("isboy"));
                        userData.setIntroduce(object.getString("introduce"));
                        AVFile file = object.getAVFile("head");
                        if(file != null){
                            userData.setHeadUrl(file.getUrl());
                        }
                        userDataList.add(userData);
                    }
                }
                listener.getUserList(userDataList);
                Log.d(TAG,userDataList.toString());
            }
        });
    }

    public interface OnUserGet{
        void getUserData(UserData userData);
        void onError(AVException e);
    }

    public static void getUserById(String userId, final OnUserGet onUserGet){
        UserCacheUtil.getInstance().getUserDataAsyncById(userId, new UserCacheUtil.OnUserDataGet() {
            @Override
            public void onUserFind(UserData userData) {
                onUserGet.getUserData(userData);
            }
        });
    }
}
