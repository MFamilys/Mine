package com.mfamilys.mine.suppost.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mfamilys.mine.R;
import com.mfamilys.mine.api.Dailyapi;
import com.mfamilys.mine.database.cache.ICache;
import com.mfamilys.mine.model.daily.StoryBean;
import com.mfamilys.mine.suppost.HttpUtil;
import com.mfamilys.mine.suppost.Settings;
import com.mfamilys.mine.ui.daily.DailyDetailsActivity;
import com.mfamilys.mine.suppost.adapter.DailyAdapter.ViewHolder;
/**
 * Created by mfamilys on 16-4-8.
 */
public  class DailyAdapter extends BaseListAdapter<StoryBean,ViewHolder>{
    public DailyAdapter(Context context, ICache<StoryBean> cache){
        super(context,cache);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily,parent,false);
        ViewHolder vh=new ViewHolder(itemView);
        return vh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final  int position) {
        final StoryBean storyBean=getItem(position);
        holder.title.setText(storyBean.getTitle());
        if(Settings.noPicMode&& HttpUtil.isWiFi==false){
            holder.image.setImageURI(null);
        }else{
            holder.image.setImageURI(Uri.parse(storyBean.getImages()[0]));
        }
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(mContext.getString(R.string.id_url), Dailyapi.daily_details_url+storyBean.getId());
                bundle.putString(mContext.getString(R.string.id_title),storyBean.getTitle());
                bundle.putString(mContext.getString(R.string.id_body),storyBean.getBody());
                bundle.putString(mContext.getString(R.string.id_imageurl),storyBean.getLargepic());
                bundle.putString(mContext.getString(R.string.id_small_image),storyBean.getImages()[0]);
                bundle.putInt(mContext.getString(R.string.id_id),storyBean.getId());
                if(isCollection){
                    bundle.putBoolean(mContext.getString(R.string.id_collection),true);
                }else {
                    bundle.putBoolean(mContext.getString(R.string.id_collection), storyBean.isCollected());
                }
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private TextView title;
        private SimpleDraweeView image;
        public ViewHolder(View itemView){
            super(itemView);
            parentView=itemView;
            title=(TextView)parentView.findViewById(R.id.title);
            image=(SimpleDraweeView)parentView.findViewById(R.id.image);
        }

    }
}
