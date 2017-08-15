package com.zia.magiccard.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zxzhu on 2017/8/2.
 */

public abstract class BaseFragment extends Fragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getResourceId(),container,false);
        findWidgets();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onCreated();
    }

    //做数据或其他初始化的方法
    protected abstract void findWidgets();

    //获取当前fragment的view的方法
    protected abstract int getResourceId();

    protected abstract void onCreated();

    //View快捷绑定id的方法
    public <T extends View> T $(int id) {
        return (T) mView.findViewById(id);
    }
}
