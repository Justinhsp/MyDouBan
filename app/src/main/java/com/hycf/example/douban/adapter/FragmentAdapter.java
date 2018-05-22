package com.hycf.example.douban.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Hui on 2018/3/5.
 * 功能：主页引导栏的三个Fragment页面设置适配器
 */

public class FragmentAdapter extends FragmentPagerAdapter{

    private List<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
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
