package com.mfamilys.mine.ui.daily;

import android.support.v7.widget.RecyclerView;

import com.mfamilys.mine.database.cache.cache.DailyCache;
import com.mfamilys.mine.suppost.adapter.DailyAdapter;
import com.mfamilys.mine.ui.support.BaseListFragment;

/**
 * Created by mfamilys on 16-4-8.
 */
public class DailyFragment extends BaseListFragment {

    @Override
    protected boolean setHeaderTab() {
        return false;
    }

    @Override
    protected void onCreateCache() {
        cache = new DailyCache(handler);
    }

    @Override
    protected RecyclerView.Adapter bindAdapter() {return new DailyAdapter(getContext(),cache);}


    @Override
    protected void loadFromNet() {
        cache.load();
    }

    @Override
    protected void loadFromCache() {
        cache.loadFromCache();
    }

    @Override
    protected boolean hasData() {
        return cache.hasData();
    }

    @Override
    protected void getArgs() {

    }
}
