package com.zia.magiccard.View;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.roughike.bottombar.BottomBar;
import com.zia.magiccard.Adapter.ConversationRecyclerAdapter;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Presenter.MainPresenter;
import com.zia.magiccard.Presenter.MainPresenterImp;
import com.zia.magiccard.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MainActivityImp {

    private BottomBar bottomBar;
    private ViewPager viewPager;
    private MainPresenterImp presenterImp;
    private ImageView titleImage;
    //消息界面的变量
    public static List<ConversationData> conversationList;
    public static ConversationRecyclerAdapter adapter;

    @Override
    protected void onCreated() {
        presenterImp.setViewPager();
        presenterImp.setBottomBar();
        adapter = new ConversationRecyclerAdapter(this);
    }

    @Override
    protected void findWidgets() {
        conversationList = new ArrayList<>();
        presenterImp = new MainPresenter(this);
        bottomBar = $(R.id.main_bottomBar);
        viewPager = $(R.id.main_viewPager);
        titleImage = $(R.id.toolbar_right);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    public BottomBar getBottomBar() {
        return bottomBar;
    }

    @Override
    public ViewPager getViewPager() {
        return viewPager;
    }

    @Override
    public FragmentManager getFragmentsManager() {
        return getSupportFragmentManager();
    }

    @Override
    public ImageView getToolbarImage() {
        return titleImage;
    }
}
