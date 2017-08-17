package com.zia.magiccard.Presenter;

import android.content.Intent;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.zia.magiccard.Base.BaseImp;
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
    }

    @Override
    public void gotoLoginPage() {
        imp.getActivity().startActivity(new Intent(imp.getActivity(), LoginActivity.class));
    }

    @Override
    public void openService() {
        AVOSCloud.setDebugLogEnabled(true);
        AVOSCloud.initialize(imp.getActivity(),"0f7PFLssqz1aLp6PrOAlakNt-gzGzoHsz","IjQEGo2YnJtWRv9shnoxRDjC");
        AVInstallation.getCurrentInstallation().saveInBackground();
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
