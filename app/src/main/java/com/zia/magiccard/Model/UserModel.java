package com.zia.magiccard.Model;

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

public class UserModel {

    private Context context;
    private static final String TAG = "UserModelTest";

    public interface OnUserGetListener{
        void getUserList(List<UserData> userDataList);
    }

    public UserModel(Context context){
        this.context = context;
    }

    public void SearchUserByNickName(final String nickname, final OnUserGetListener listener) {
        AVQuery<AVObject> userQuery = new AVQuery<>("_User");
        userQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                List<UserData> userDataList = new ArrayList<>();
                for (AVObject object: list){
                    if(object.getString("nickname").contains(nickname)){
                        UserData userData = new UserData();
                        userData.setNickname(object.getString("nickname"));
                        userData.setInstallationId(object.getString("installationId"));
                        userData.setUsername(object.getString("username"));
                        userData.setObjectId(object.getObjectId());
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

    public void getUserById(String userId, final OnUserGet onUserGet){
        AVQuery<AVUser> avQuery = new AVQuery<>("_User");
        avQuery.getInBackground(userId, new GetCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if(e != null){
                    onUserGet.onError(e);
                }
                else{
                    UserData userData = new UserData();
                    userData.setObjectId(avUser.getObjectId());
                    userData.setUsername(avUser.getUsername());
                    userData.setInstallationId(avUser.getString("installationId"));
                    userData.setIntroduce(avUser.getString("introduce"));
                    userData.setIsboy(avUser.getBoolean("isboy"));
                    userData.setNickname(avUser.getString("nickname"));
                    AVFile file = avUser.getAVFile("head");
                    userData.setHeadUrl(file.getUrl());
                    Log.d(TAG,userData.toString());
                    onUserGet.getUserData(userData);
                }
            }
        });
    }
}
