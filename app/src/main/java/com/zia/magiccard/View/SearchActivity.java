package com.zia.magiccard.View;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zia.magiccard.Adapter.UserRecyclerAdapter;
import com.zia.magiccard.Base.BaseActivity;
import com.zia.magiccard.Presenter.RecyclerViewPresenter;
import com.zia.magiccard.Presenter.RecyclerViewPresenterImp;
import com.zia.magiccard.Presenter.SearchPresenter;
import com.zia.magiccard.Presenter.SearchPresenterImp;
import com.zia.magiccard.R;
import com.zia.magiccard.Util.MyRecyclerView;
import com.zia.magiccard.View.Fragments.RecyclerViewImp;

public class SearchActivity extends BaseActivity implements SearchActivityImp,RecyclerViewImp {

    private LinearLayout linearLayout,root;
    private EditText editText;
    private SearchPresenterImp presenterImp;
    private MyRecyclerView recyclerView;
    private UserRecyclerAdapter adapter;
    private RecyclerViewPresenterImp recyclerViewPresenterImp;
    private CardView cardView;

    @Override
    protected void onCreated() {
        //初始化recycler
        recyclerViewPresenterImp.setRecyclerView();
        //设置recycler不能侧滑
        recyclerView.setExtraViewId(0);
        //设置recycler item触摸监听
        recyclerView.setMyListener(new MyRecyclerView.MyListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenterImp.gotoChatPage(adapter.getUserDataList().get(position),view);
            }

            @Override
            public void onDeleteClick(int position) {

            }
        });
        //点击搜索框
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                //启动位移动画
                presenterImp.moveEditText();
            }
        });
        //点击外部区域返回
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //EditText文字监听
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //当字改变后，搜索该用户并显示在recycler上
                presenterImp.searchUser();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //设置背景监听
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void findWidgets() {
        presenterImp = new SearchPresenter(this);
        recyclerViewPresenterImp = new RecyclerViewPresenter(this);
        linearLayout = $(R.id.search_layout);
        editText = $(R.id.search_edit);
        root = $(R.id.search_root);
        recyclerView = $(R.id.search_recycler);
        adapter = new UserRecyclerAdapter(this);
        cardView = $(R.id.search_CardView);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_search;
    }

    @Override
    protected void beforeSetContentView() {

    }

    @Override
    public LinearLayout getEditLayout() {
        return linearLayout;
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

    @Override
    public UserRecyclerAdapter getPersonAdapter() {
        return adapter;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }
}
