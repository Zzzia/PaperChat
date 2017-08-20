package com.zia.magiccard.Presenter;

import android.content.Intent;
import android.view.View;

import com.zia.magiccard.Base.BaseImp;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.View.ChatActivity;

/**
 * Created by zia on 17-8-17.
 */

public class MessagePresenterPresenter implements MessagePresenterImp {

    private BaseImp fragmentImp;

    public MessagePresenterPresenter(BaseImp fragmentImp){
        this.fragmentImp = fragmentImp;
    }

    @Override
    public void gotoChatPage(ConversationData conversationData, View view) {
        Intent intent = new Intent(fragmentImp.getActivity(), ChatActivity.class);
        intent.putExtra("conversationData",conversationData);
        PageUtil.gotoPageWithCard(fragmentImp.getActivity(),view,intent);
    }

}
