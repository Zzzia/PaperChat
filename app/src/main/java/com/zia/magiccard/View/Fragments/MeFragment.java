package com.zia.magiccard.View.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.bumptech.glide.Glide;
import com.zia.magiccard.Adapter.MarkdownChooseAdapter;
import com.zia.magiccard.Base.BaseFragment;
import com.zia.magiccard.Presenter.MeFragmentPresenter;
import com.zia.magiccard.Presenter.MePresenterImp;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MessageUtil;
import com.zia.magiccard.Util.ScreenUtil;
import com.zia.magiccard.View.ChangeActivity;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.LoginActivity;
import com.zia.magiccard.View.MainActivity;

public class MeFragment extends BaseFragment implements MeFragmentImp {

    private TextView loginOutButton,nickname,introduce,markdown,help;
    private CardView cardView;
    public static ImageView head;
    private MePresenterImp presenterImp;
    private LinearLayout rootView;


    @Override
    protected void findWidgets() {
        loginOutButton = $(R.id.me_login_out);
        cardView = $(R.id.me_CardView);
        head = $(R.id.me_head);
        nickname = $(R.id.me_name);
        introduce = $(R.id.me_introduce);
        markdown = $(R.id.me_markdown);
        help = $(R.id.me_help);
        rootView = $(R.id.me_root);
        presenterImp = new MeFragmentPresenter(this);
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void onCreated() {
        presenterImp.initData();
        loginOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageUtil.getInstance().loginOut();
                AVUser.logOut();
                MainActivity.conversations = null;
                MainActivity.conversationRecyclerAdapter = null;
                MainActivity.markdownDatas = null;
                MainActivity.classifyDatas = null;
                ChatActivity.adapter = null;
                ChatActivity.currentConversationId = null;
                presenterImp.gotoLoginActivity();
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterImp.gotoChangeActivity();
            }
        });
        markdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.choose_markdown,null);
                TextView textView = v.findViewById(R.id.choose_markdown_save);
                TextView write = v.findViewById(R.id.choose_markdown_new);
                RecyclerView recyclerView = v.findViewById(R.id.choose_markdown_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                MarkdownChooseAdapter adapter = new MarkdownChooseAdapter(getContext(),MainActivity.markdownDatas);
                adapter.setCanPreview(true,rootView);
                recyclerView.setAdapter(adapter);
                textView.setVisibility(View.GONE);
                write.setVisibility(View.GONE);
                int width = ScreenUtil.bulid(getActivity()).getPxWide();
                int height = ScreenUtil.bulid(getActivity()).getPxHiget();
                PopupWindow popupWindow = new PopupWindow(v,width/6*5,height/4*3);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAtLocation(rootView, Gravity.CENTER,0,0);
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast("好运总是给那些善于发现的人");
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        presenterImp.initData();
    }

    @Override
    public ImageView getHead() {
        return head;
    }

    @Override
    public TextView getNickname() {
        return nickname;
    }

    @Override
    public TextView getIntroduce() {
        return introduce;
    }
}
