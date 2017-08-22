package com.zia.magiccard.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zia.magiccard.Base.BaseImp;
import com.zia.magiccard.Bean.UserData;

/**
 * Created by zia on 17-8-22.
 */

public interface AddFriendImp extends BaseImp {
    Button getAddButton();
    TextView getNickname();
    ImageView getHead();
    TextView getSex();
    TextView getIntroduce();
    UserData getUserData();
    LinearLayout getRootView();
}
