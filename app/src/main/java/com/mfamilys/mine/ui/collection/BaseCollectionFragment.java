package com.mfamilys.mine.ui.collection;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.mfamilys.mine.R;
import com.mfamilys.mine.suppost.adapter.PagerAdapter;
import com.mfamilys.mine.ui.support.AbsTopNavigationFragment;

/**
 * Created by mfamilys on 16-4-10.
 */
public class BaseCollectionFragment extends AbsTopNavigationFragment {
    private PagerAdapter pagerAdapter;

    @Override
    protected PagerAdapter initPagerAdapter() {
        String[] tabTitles={getContext().getString(R.string.daily)};
        pagerAdapter=new PagerAdapter(getChildFragmentManager(),tabTitles){
            @Override
            public Fragment getItem(int position) {
                Fragment fragment=new CollectionFragment();
                Bundle bundle=new Bundle();
                bundle.putInt(getString(R.string.id_pos),position);
                fragment.setArguments(bundle);
                return fragment;
            }
        };
        return pagerAdapter;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getChildFragmentManager().getFragments()!=null){
            getChildFragmentManager().getFragments().clear();
        }
    }
}
