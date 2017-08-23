package com.zia.magiccard.Presenter;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zia.magiccard.Adapter.ViewPagerAdapter;
import com.zia.magiccard.Bean.ClassifyData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.View.Fragments.FriendFragment;
import com.zia.magiccard.View.MainActivity;
import com.zia.magiccard.View.MainActivityImp;
import com.zia.magiccard.View.SearchActivity;

import java.util.ArrayList;

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
                                final Dialog dialog = new Dialog(activityImp.getActivity());
                                dialog.show();
                                final Window window  = dialog.getWindow();
                                assert window != null;
                                window.setBackgroundDrawable(new ColorDrawable(0));//背景透明
                                window.setContentView(R.layout.classify_dialog);
                                final EditText editText = window.findViewById(R.id.classify_dialog_edit);
                                Button button = window.findViewById(R.id.classify_dialog_button);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(editText.getText() != null && !editText.getText().toString().isEmpty()){
                                            ClassifyData classifyData = new ClassifyData();
                                            classifyData.setUserDatas(new ArrayList<UserData>());
                                            classifyData.setClassName(editText.getText().toString());
                                            MainActivity.classifyDatas.add(classifyData);
                                            Log.d("123123",MainActivity.classifyDatas.toString());
                                            FriendFragment.adapter.freshData();
                                            dialog.hide();
                                            PushUtil.pushClassifyData();
                                        }
                                    }
                                });
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
