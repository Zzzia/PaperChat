package com.zia.magiccard.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zia.magiccard.View.Fragments.FriendFragment;
import com.zia.magiccard.View.Fragments.MessageFragment;
import com.zia.magiccard.View.Fragments.MeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zia on 17-8-16.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(new MessageFragment());
        fragments.add(new FriendFragment());
        fragments.add(new MeFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
