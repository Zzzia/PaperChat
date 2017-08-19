package com.zia.magiccard.Model;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
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
}
