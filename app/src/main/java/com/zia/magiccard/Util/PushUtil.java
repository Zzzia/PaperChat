package com.zia.magiccard.Util;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.zia.magiccard.View.MainActivity;

import java.util.List;

/**
 * Created by zia on 17-8-21.
 */

public class PushUtil {

    private static final String TAG=  "PushUtilTest";


    /**
     * 保存mainActivity中的conversationList到服务器
     */
    public static void saveConversations(){
        AVQuery<AVObject> query = new AVQuery<>("UserData");
        query.whereEqualTo("userId",AVUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e == null){
                    AVObject avObject = null;
                    if(list.size() == 0){//服务器上没有数据，新建
                        avObject = new AVObject("UserData");
                        avObject.put("userId", AVUser.getCurrentUser().getObjectId());
                    }else{//服务器上有数据，直接更新数据
                        avObject = list.get(0);
                    }
                    avObject.put("conversationList",MainActivity.conversationList);
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e == null){
                                Log.d(TAG,"保存Conversations成功！");
                            }else{
                                e.printStackTrace();
                                Log.d(TAG,"保存Conversations失败！");
                            }
                        }
                    });
                }else{
                    e.printStackTrace();
                    Log.d(TAG,"query.findInBackground error!");
                }
            }
        });


    }
}
