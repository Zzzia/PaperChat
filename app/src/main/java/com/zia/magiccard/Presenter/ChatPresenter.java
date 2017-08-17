package com.zia.magiccard.Presenter;

import com.zia.magiccard.View.ChatImp;

/**
 * Created by zia on 17-8-17.
 */

public class ChatPresenter implements ChatPresenterImp {

    private ChatImp imp;

    public ChatPresenter(ChatImp imp){
        this.imp = imp;
    }

}
