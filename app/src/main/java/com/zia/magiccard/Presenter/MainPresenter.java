package com.zia.magiccard.Presenter;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AlphaAnimation;

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
    }

    @Override
    public void setBottomBar() {
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                AlphaAnimation appearAnimation = new AlphaAnimation(0,1);
                appearAnimation.setDuration(200);
                AlphaAnimation dismissAnimation = new AlphaAnimation(1,0);
                dismissAnimation.setDuration(200);
                activityImp.getToolbarImage().startAnimation(dismissAnimation);
                activityImp.getToolbarImage().startAnimation(appearAnimation);

                switch (tabId){
                    case R.id.main:
                        activityImp.getToolbarImage().setImageResource(R.mipmap.ic_person_add_white_18dp);
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.friends:
                        activityImp.getToolbarImage().setImageResource(R.mipmap.ic_library_add_white_18dp);
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
}
