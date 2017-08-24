package com.zia.magiccard.View;

import android.app.ProgressDialog;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.zia.magiccard.Adapter.MessageRecyclerAdapter;
import com.zia.magiccard.Base.BaseImp;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Presenter.RecyclerViewPresenterImp;
import com.zia.magiccard.View.Fragments.RecyclerViewImp;

/**
 * Created by zia on 17-8-17.
 */

public interface ChatImp extends BaseImp {
    UserData getUserData();
    ConversationData getConversationData();
    EditText getEditText();
    MessageRecyclerAdapter getMessageAdapter();
    RecyclerView getRecyclerView();
    ProgressDialog getDialog();
}
