package com.mfamilys.mine.ui.support;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mfamilys.mine.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by mfamilys on 16-4-10.
 */
public abstract  class AbsTopNavigationFragment extends Fragment{
    protected View parentView;
    protected ViewPager viewPager;
    protected PagerAdapter pagerAdapter;
    private SmartTabLayout smartTabLayout;
    protected abstract PagerAdapter initPagerAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView=View.inflate(getContext(), R.layout.layout_top_navigation,null);
        viewPager=(ViewPager)parentView.findViewById(R.id.inner_viewpager);
        smartTabLayout=(SmartTabLayout)getActivity().findViewById(R.id.tab_layout);
        pagerAdapter=initPagerAdapter();
        smartTabLayout.setVisibility(View.VISIBLE);
        viewPager.setAdapter(pagerAdapter);
        smartTabLayout.setViewPager(viewPager);
        return parentView;
    }

}

