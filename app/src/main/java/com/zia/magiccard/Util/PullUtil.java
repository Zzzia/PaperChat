package com.zia.magiccard.Util;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationsQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.google.gson.Gson;
import com.zia.magiccard.Bean.ClassifyData;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.MarkdownData;
import com.zia.magiccard.Bean.UserData;
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
        //加载个人信息，没有缓存
        UserCacheUtil.getInstance().getUserDataAsync(AVUser.getCurrentUser().getObjectId(), new UserCacheUtil.OnUserDataGet() {
            @Override
            public void onUserFind(UserData userData) {
                MainActivity.userData = userData;
                Log.e("currentUserData",userData.toString());
            }
        });
    }

    public static void pullConversationList() {
//        AVQuery<AVObject> avQuery = new AVQuery<>("UserData");
//        avQuery.whereEqualTo("userId", AVUser.getCurrentUser().getObjectId());
//        avQuery.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//                if (e == null) {
//                    if (list.size() == 0) return;
//                    AVObject avObject = list.get(0);
//                    MainActivity.conversationList.clear();
//                    try {
//                        for (int i = 0; i < avObject.getJSONArray("conversationList").length(); i++) {
//                            Log.d(TAG, "i: " + i);
//                            Gson gson = new Gson();
//                            ConversationData conversationData = gson.fromJson(avObject.getJSONArray("conversationList").get(i).toString(), ConversationData.class);
//                            Log.d(TAG, conversationData.toString());
//                            MainActivity.conversationList.add(conversationData);
//                        }
//                        MainActivity.conversationRecyclerAdapter.freshMessageList(MainActivity.conversationList);
//                    } catch (JSONException e1) {
//                        e1.printStackTrace();
//                    }
//                } else {
//                    e.printStackTrace();
//                    Log.d(TAG, "pullConversationList error!");
//                }
//            }
//        });
          MessageUtil.getInstance().getClient().open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                AVIMConversationsQuery query = avimClient.getConversationsQuery();
                query.setLimit(30);
                query.findInBackground(new AVIMConversationQueryCallback() {
                    @Override
                    public void done(List<AVIMConversation> list, AVIMException e) {
                        if(e == null){
                            Log.e(TAG,"conversation Size: "+list.size());
                            for (int i = 0; i < list.size(); i++) {
                                AVIMConversation conversation = list.get(i);
                                ConversationData conversationData = new ConversationData();
                                conversationData.setConversationId(conversation.getConversationId());
                                //conversationData.setLastContent(conversation.getLastMessage().getContent());
                                conversationData.setMembers(conversation.getMembers());
                                if(conversation.getMembers().size() == 2){
                                    for (int j = 0; j < list.size(); j++) {
                                        if(conversation.getMembers().get(i).equals(AVUser.getCurrentUser().getObjectId()))
                                            conversation.getMembers().remove(i);
                                    }
                                    conversation.getMembers().remove(AVUser.getCurrentUser().getObjectId());
                                    try {
                                        UserData userData = UserCacheUtil.getInstance().getUserDataById(conversation.getMembers().get(0));
                                        conversationData.setName(userData.getNickname());
                                        conversationData.setImageUrl(userData.getHeadUrl());
                                    } catch (AVException e1) {
                                        e1.printStackTrace();
                                    }
                                }
//                                try {
//                                    conversationData.setName(ConversationUtil.getConversationName(conversation.getMembers()));
//                                } catch (AVException e1) {
//                                    e1.printStackTrace();
//                                }
                                conversationData.setUnreadCount(conversation.getUnreadMessagesCount());
                                conversationData.setTime(conversation.getLastMessageAt().getTime());
                                MainActivity.conversationList.add(conversationData);
                                Log.e(TAG, conversationData.toString());
                            }
                            MainActivity.conversationRecyclerAdapter.freshMessageList(MainActivity.conversationList);
                        }else e.printStackTrace();
                    }
                });
            }
        });
    }

    /**
     * 拉取markdown数据
     */
    public static void pullMarkdownData(){
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
                        for (int i = 0; i < avObject.getJSONArray("markdownList").length(); i++) {
                            Log.d(TAG, "i: " + i);
                            Gson gson = new Gson();
                            MarkdownData markdownData = gson.fromJson(avObject.getJSONArray("markdownList").get(i).toString(), MarkdownData.class);
                            Log.d(TAG, markdownData.toString());
                            MainActivity.markdownDatas.add(markdownData);
                        }
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
