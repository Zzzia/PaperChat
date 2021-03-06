package com.zia.magiccard.View;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.zia.magiccard.Base.BaseImp;

/**
 * Created by zia on 17-8-16.
 */

public interface MainActivityImp extends BaseImp {
    BottomBar getBottomBar();
    ViewPager getViewPager();
    FragmentManager getFragmentsManager();
    ImageView getToolbarImage();
    LinearLayout getRoot();
}
