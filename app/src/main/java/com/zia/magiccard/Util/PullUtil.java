package com.zia.magiccard.Util;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.google.gson.Gson;
import com.zia.magiccard.Bean.ClassifyData;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Model.UserModel;
import com.zia.magiccard.View.Fragments.FriendFragment;
import com.zia.magiccard.View.MainActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-21.
 * 从服务器上获取数据的工具类
 */

public class PullUtil {

    private static final String TAG = "PullUtilTest";

    public static void pullCurrentUserData() {
        UserModel userModel = new UserModel();
        userModel.getUserById(AVUser.getCurrentUser().getObjectId(), new UserModel.OnUserGet() {
            @Override
            public void getUserData(UserData userData) {
                MainActivity.userData = userData;
            }

            @Override
            public void onError(AVException e) {
                e.printStackTrace();
            }
        });
    }

    public static void pullConversationList() {
        AVQuery<AVObject> avQuery = new AVQuery<>("UserData");
        avQuery.whereEqualTo("userId", AVUser.getCurrentUser().getObjectId());
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() == 0) return;
                    AVObject avObject = list.get(0);
                    MainActivity.conversationList.clear();
                    try {
                        for (int i = 0; i < avObject.getJSONArray("conversationList").length(); i++) {
                            Log.d(TAG, "i: " + i);
                            Gson gson = new Gson();
                            ConversationData conversationData = gson.fromJson(avObject.getJSONArray("conversationList").get(i).toString(), ConversationData.class);
                            Log.d(TAG, conversationData.toString());
                            MainActivity.conversationList.add(conversationData);
                        }
                        MainActivity.conversationRecyclerAdapter.freshMessageList(MainActivity.conversationList);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    e.printStackTrace();
                    Log.d(TAG, "pullConversationList error!");
                }
            }
        });
    }

    /**
     * 获取分组信息
     */
    public static void pullClassifyData() {
        AVQuery<AVObject> avQuery = new AVQuery<>("UserData");
        avQuery.whereEqualTo("userId", AVUser.getCurrentUser().getObjectId());
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() == 0) return;
                    AVObject avObject = list.get(0);
                    MainActivity.classifyDatas.clear();
                    try {
                        //如果网上有数据
                        if (avObject.getJSONArray("classifyList") != null && avObject.getJSONArray("classifyList").length() != 0) {
                            for (int i = 0; i < avObject.getJSONArray("classifyList").length(); i++) {
                                Log.d(TAG, "i: " + i);
                                Gson gson = new Gson();
                                ClassifyData classifyData = gson.fromJson(avObject.getJSONArray("classifyList").get(i).toString(), ClassifyData.class);
                                Log.d(TAG, classifyData.toString());
                                MainActivity.classifyDatas.add(classifyData);
                            }
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    if (MainActivity.classifyDatas.size() == 0) {
                        Log.d(TAG,"MainActivity.classifyDatas.size() == 0 ,没有数据，创建我的好友栏");
                        //如果没有数据，则创建我的好友栏
                        ClassifyData classifyData = new ClassifyData();
                        classifyData.setClassName("我的好友");
                        List<UserData> userDataList = new ArrayList<UserData>();
                        classifyData.setUserDatas(userDataList);
                        MainActivity.classifyDatas.add(classifyData);
                    }
                    FriendFragment.adapter.freshData();
                } else {
                    e.printStackTrace();
                    Log.d(TAG, "pullConversationList error!");
                }
            }
        });
    }
}
