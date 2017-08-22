package com.zia.magiccard.View;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Presenter.AddFriendPresenter;
import com.zia.magiccard.Presenter.AddFriendPresenterImp;
import com.zia.magiccard.R;

public class AddFriendActivity extends BaseActivity implements AddFriendImp {

    private AddFriendPresenterImp presenterImp;
    private Button addFriend;
    private TextView introduce,sex,nickname;
    private ImageView head;
    private LinearLayout rootView;
    private CardView cardView;

    @Override
    protected void onCreated() {
        //加载数据信息
        presenterImp.initData();
        //添加好友按钮监听
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenterImp.showPopWindow();
            }
        });
        //点击背景返回
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void findWidgets() {
        addFriend = $(R.id.add_friend_add);
        introduce = $(R.id.add_friend_introduce);
        sex = $(R.id.add_friend_sex);
        nickname = $(R.id.add_friend_nickname);
        head = $(R.id.add_friend_head);
        rootView = $(R.id.add_friend_root);
        cardView = $(R.id.add_friend_CardView);
        presenterImp = new AddFriendPresenter(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    public Button getAddButton() {
        return addFriend;
    }

    @Override
    public TextView getNickname() {
        return nickname;
    }

    @Override
    public ImageView getHead() {
        return head;
    }

    @Override
    public TextView getSex() {
        return sex;
    }

    @Override
    public TextView getIntroduce() {
        return introduce;
    }

    @Override
    public UserData getUserData() {
        if(getIntent().getSerializableExtra("userData") != null){
            return (UserData)getIntent().getSerializableExtra("userData");
        }
        return null;
    }

    @Override
    public LinearLayout getRootView() {
        return rootView;
    }
}
