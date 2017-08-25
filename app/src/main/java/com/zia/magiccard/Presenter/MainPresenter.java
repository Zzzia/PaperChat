package com.zia.magiccard.Presenter;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zia.magiccard.Adapter.ChooseAdapter;
import com.zia.magiccard.Adapter.ViewPagerAdapter;
import com.zia.magiccard.Bean.ClassifyData;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.PageUtil;
import com.zia.magiccard.Util.PushUtil;
import com.zia.magiccard.Util.ScreenUtil;
import com.zia.magiccard.View.ChatActivity;
import com.zia.magiccard.View.Fragments.FriendFragment;
import com.zia.magiccard.View.MainActivity;
import com.zia.magiccard.View.MainActivityImp;
import com.zia.magiccard.View.SearchActivity;

import java.util.ArrayList;
import java.util.List;

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
                        activityImp.getToolbarImage().setImageResource(R.mipmap.ic_add_white_24dp);
                        viewPager.setCurrentItem(0);
                        activityImp.getToolbarImage().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View menu = LayoutInflater.from(activityImp.getActivity()).inflate(R.layout.add_menu,null);
                                TextView search = menu.findViewById(R.id.menu_add_friend);
                                TextView qun = menu.findViewById(R.id.menu_add_qun);
                                final PopupWindow popupWindow = new PopupWindow(menu, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                popupWindow.setFocusable(true);
                                popupWindow.setOutsideTouchable(true);
                                popupWindow.showAsDropDown(activityImp.getToolbarImage());
                                search.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        PageUtil.gotoPageWithCard(activityImp.getActivity(),activityImp.getViewPager(), SearchActivity.class);
                                        popupWindow.dismiss();
                                    }
                                });
                                qun.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        View v = LayoutInflater.from(activityImp.getActivity()).inflate(R.layout.choose_friends,null);
                                        RecyclerView recyclerView = v.findViewById(R.id.choose_friends_recycler);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(activityImp.getActivity()));
                                        List<UserData> userDataList = new ArrayList<UserData>();
                                        for(ClassifyData classifyData : MainActivity.classifyDatas){
                                            userDataList.addAll(classifyData.getUserDatas());
                                        }
                                        int width = ScreenUtil.bulid(activityImp.getActivity()).getPxWide();
                                        int height = ScreenUtil.bulid(activityImp.getActivity()).getPxHiget();
                                        final ChooseAdapter adapter = new ChooseAdapter(activityImp.getActivity(),userDataList);
                                        recyclerView.setAdapter(adapter);
                                        final PopupWindow selector = new PopupWindow(v,width/4*3,height/4*3);
                                        TextView textView = v.findViewById(R.id.choose_friends_save);
                                        textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ConversationData conversationData = new ConversationData();
                                                List<String> members = new ArrayList<String>();
                                                for(UserData userData : adapter.getSelecteds()){
                                                    members.add(userData.getObjectId());
                                                }
                                                Log.e("select",members.toString());
                                                conversationData.setMembers(members);
                                                Intent intent = new Intent(activityImp.getActivity(), ChatActivity.class);
                                                intent.putExtra("conversationData",conversationData);
                                                PageUtil.gotoPageWithCard(activityImp.getActivity(),selector.getContentView(),intent);
                                            }
                                        });
                                        selector.setFocusable(true);
                                        selector.setOutsideTouchable(true);
                                        selector.showAtLocation(activityImp.getRoot(), Gravity.CENTER,0,0);
                                        popupWindow.dismiss();
                                    }
                                });
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
