package com.zia.magiccard.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zia.magiccard.Base.BaseImp;

/**
 * Created by zia on 17-8-20.
 */

public interface ChangeActivityImp extends BaseImp {
    EditText getEditText();
    boolean isBoy();
    ImageView getHeadImage();
    String getBitmapPath();
    Button getSaveButton();
    void setBoy();
    void setGirl();
}
