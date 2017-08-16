package com.zia.magiccard.Presenter;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zia.magiccard.Adapter.ViewPagerAdapter;
import com.zia.magiccard.R;
import com.zia.magiccard.View.MainActivityImp;

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
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.main:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.friends:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.me:
                        viewPager.setCurrentItem(2);
                        break;
                }
            }
        });
    }
}
