package com.zia.magiccard.View;

import android.widget.EditText;
import android.widget.LinearLayout;

import com.zia.magiccard.Adapter.UserRecyclerAdapter;
import com.zia.magiccard.Base.BaseImp;

/**
 * Created by zia on 17-8-19.
 */

public interface SearchActivityImp extends BaseImp {
    LinearLayout getEditLayout();
    EditText getEditText();
    UserRecyclerAdapter getPersonAdapter();
}
