package com.mfamilys.mine.ui.collection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mfamilys.mine.R;
import com.mfamilys.mine.database.cache.collection.CollectionDailyCache;
import com.mfamilys.mine.suppost.CONSTANT;
import com.mfamilys.mine.suppost.adapter.DailyAdapter;
import com.mfamilys.mine.ui.support.BaseListFragment;

/**
 * Created by mfamilys on 16-4-10.
 */
public class CollectionFragment extends BaseListFragment{
    private int pos;

    @Override
    protected void setLayout() {
        mLayout= R.layout.layout_collection_list;
    }

    @Override
    protected boolean setRefreshView() {
        return false;
    }

    @Override
    protected void onCreateCache() {
        switch (pos){
            case 0:
                cache=new CollectionDailyCache(handler);
                break;
        }
    }

    @Override
    protected RecyclerView.Adapter bindAdapter() {
        switch (pos){
            case 0:
                return new DailyAdapter(getContext(),cache);
        }
        return null;
    }

    @Override
    protected void loadFromNet() {
        handler.sendEmptyMessage(CONSTANT.ID_FAILURE);
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
        pos=getArguments().getInt(getString(R.string.id_pos));
    }
}
