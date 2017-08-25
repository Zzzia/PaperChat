package com.zia.magiccard.View;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.roughike.bottombar.BottomBar;
import com.zia.magiccard.Adapter.ConversationRecyclerAdapter;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Bean.ClassifyData;
import com.zia.magiccard.Bean.ConversationData;
import com.zia.magiccard.Bean.MarkdownData;
import com.zia.magiccard.Bean.UserData;
import com.zia.magiccard.Model.CustomConversationEventHandler;
import com.zia.magiccard.Model.CustomMessageHandler;
import com.zia.magiccard.Presenter.MainPresenter;
import com.zia.magiccard.Presenter.MainPresenterImp;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MessageUtil;
import com.zia.magiccard.Util.PullUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MainActivityImp {

    private BottomBar bottomBar;
    private ViewPager viewPager;
    private MainPresenterImp presenterImp;
    private ImageView titleImage;
    private LinearLayout root;
    //消息界面的变量
    public static List<ConversationData> conversationList = new ArrayList<>();
    public static ConversationRecyclerAdapter conversationRecyclerAdapter;
    //当前登录用户信息
    public static UserData userData = null;
    //联系人列表变量
    public static List<ClassifyData> classifyDatas = null;

    public static List<MarkdownData> markdownDatas = null;

    @Override
    protected void onCreated() {
        //关闭缓存
        AVIMClient.setMessageQueryCacheEnable(false);
        //未读消息开启
        AVIMClient.setUnreadNotificationEnabled(true);
        //开启错误调试日志
        AVOSCloud.setDebugLogEnabled(true);
        //得到一个消息工具的实例
        MessageUtil.getInstance();
        //初始化消息接收类
        AVIMMessageManager.registerDefaultMessageHandler(new CustomMessageHandler(this));
        //初始化事件接收类
        AVIMMessageManager.setConversationEventHandler(new CustomConversationEventHandler());
        //尝试保存installation
        AVInstallation.getCurrentInstallation().saveInBackground();
        //下载当前用户信息
        PullUtil.pullCurrentUserData();
        //下载用户好友分组信息
        PullUtil.pullClassifyData();
        presenterImp.setViewPager();
        presenterImp.setBottomBar();
        conversationRecyclerAdapter = new ConversationRecyclerAdapter(this);
        //从服务器获取对话资料加载到recycler上
        PullUtil.pullConversationList();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        userData = null;
//        conversationList = null;
//        conversationRecyclerAdapter = null;
//        classifyDatas = null;
    }

    @Override
    protected void findWidgets() {
        conversationList = new ArrayList<>();
        classifyDatas = new ArrayList<>();
        markdownDatas = new ArrayList<>();
        presenterImp = new MainPresenter(this);
        bottomBar = $(R.id.main_bottomBar);
        viewPager = $(R.id.main_viewPager);
        titleImage = $(R.id.toolbar_right);
        root = $(R.id.main_root);
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

    @Override
    public LinearLayout getRoot() {
        return root;
    }
}
