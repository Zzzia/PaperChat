package com.zia.magiccard.Presenter;

import android.content.Intent;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.zia.magiccard.Base.BaseImp;
import com.zia.magiccard.Model.CustomMessageHandler;
import com.zia.magiccard.Util.MessageUtil;
import com.zia.magiccard.View.LoginActivity;
import com.zia.magiccard.View.MainActivity;

/**
 * Created by zia on 17-8-13.
 */

public class StartPresenter implements StartImp {

    private BaseImp imp;

    public StartPresenter(BaseImp imp){
        this.imp = imp;
    }

    @Override
    public void gotoMainPage() {
        imp.getActivity().startActivity(new Intent(imp.getActivity(), MainActivity.class));
        imp.getActivity().finish();
    }

    @Override
    public void gotoLoginPage() {
        imp.getActivity().startActivity(new Intent(imp.getActivity(), LoginActivity.class));
        imp.getActivity().finish();
    }

    @Override
    public void openService() {
        //初始化leanCloud服务
        AVOSCloud.initialize(imp.getActivity(),"0f7PFLssqz1aLp6PrOAlakNt-gzGzoHsz","IjQEGo2YnJtWRv9shnoxRDjC");
        //未读消息开启
        AVIMClient.setUnreadNotificationEnabled(true);
        //开启错误调试日志
        AVOSCloud.setDebugLogEnabled(true);
        //初始化消息接收类
        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler(imp.getActivity()));
        //尝试保存installation
        AVInstallation.getCurrentInstallation().saveInBackground();
        //开启聊天室，登录用户
        AVUser user = AVUser.getCurrentUser();
        if(user == null) return;
        AVIMClient client = AVIMClient.getInstance(user);
        client.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if(e != null) e.printStackTrace();
            }
        });
        //得到一个消息工具的实例
        MessageUtil.getInstance();
    }

    @Override
    public void saveInstallationId() {
        AVUser user = AVUser.getCurrentUser();
        if(user == null) return;
        String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
        if(installationId != null) {
            user.put("installationId",installationId);
            user.saveInBackground();
        }
    }
}
