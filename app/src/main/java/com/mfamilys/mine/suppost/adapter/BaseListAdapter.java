package com.mfamilys.mine.suppost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.mfamilys.mine.database.cache.BaseCollectionCache;
import com.mfamilys.mine.database.cache.ICache;
import com.mfamilys.mine.suppost.HttpUtil;

import java.util.List;

/**
 * Created by mfamilys on 16-4-8.
 */
public abstract class BaseListAdapter<M,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<M> mItems;
    protected Context mContext;
    protected ICache<M> mCache;

    protected boolean isCollection =false;

    public BaseListAdapter(Context context,ICache<M> cache){
        mContext=context;
        mCache=cache;
        mItems=cache.getmList();
        if(cache instanceof BaseCollectionCache){
            isCollection=true;
        }
        HttpUtil.readNetworkState();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    protected M getItem(int position){return mItems.get(position);}
}
