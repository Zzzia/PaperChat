package com.zia.magiccard.Presenter;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zia.magiccard.Adapter.ViewPagerAdapter;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Model.UserModel;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.View.MainActivity;
import com.zia.magiccard.View.MainActivityImp;
import com.zia.magiccard.View.SearchActivity;

/**
 * Created by zia on 17-8-16.
 */

public class MainPresenter implements MainPresenterImp {

    private MainActivityImp activityImp;
    private ViewPager viewPager;
    private BottomBar bottomBar;
    private ViewPagerAdapter adapter = null;

    public MainPresenter(MainActivityImp imp){
        activityImp = imp;
    }


    @Override
    public void setViewPager() {
        if(adapter == null){
            adapter = new ViewPagerAdapter(activityImp.getFragmentsManager());
        }
        viewPager = activityImp.getViewPager();
        bottomBar = activityImp.getBottomBar();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.clearFocus();
                bottomBar.selectTabAtPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setBottomBar() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.main:
                        activityImp.getToolbarImage().setImageResource(R.mipmap.ic_search_white_24dp);
                        viewPager.setCurrentItem(0);
                        activityImp.getToolbarImage().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PageUtil.gotoPageWithCard(activityImp.getActivity(),activityImp.getViewPager(), SearchActivity.class);
                            }
                        });
                        break;
                    case R.id.friends:
                        activityImp.getToolbarImage().setImageResource(R.mipmap.ic_add_white_24dp);
                        activityImp.getToolbarImage().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.me:
                        activityImp.getToolbarImage().setImageResource(0);
                        viewPager.setCurrentItem(2);
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {

    }
}
