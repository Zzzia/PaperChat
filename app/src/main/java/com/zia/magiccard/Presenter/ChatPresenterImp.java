package com.zia.magiccard.Presenter;

import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMClient;

/**
 * Created by zia on 17-8-17.
 */

public interface ChatPresenterImp {
    void sendMessage();
    void initData();
    void sendAudio(TextView hintView);
    void sendPicture(String path);
}
