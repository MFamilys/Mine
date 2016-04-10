package com.mfamilys.mine.suppost.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by mfamilys on 16-4-10.
 */
public abstract class PagerAdapter  extends FragmentStatePagerAdapter {
    private String[] titles;
    public PagerAdapter(FragmentManager fm,String[] titles){
        super(fm);
        this.titles=titles;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
