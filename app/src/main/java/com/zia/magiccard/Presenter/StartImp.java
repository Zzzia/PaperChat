package com.zia.magiccard.Presenter;

import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

/**
 * Created by zia on 17-8-16.
 */

public interface StartImp {
    void gotoMainPage();
    void gotoLoginPage();
    void openService(AVIMClientCallback avimClientCallback);
    void saveInstallationId();
}
