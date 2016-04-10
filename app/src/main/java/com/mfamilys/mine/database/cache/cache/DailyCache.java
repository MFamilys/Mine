package com.mfamilys.mine.database.cache.cache;

import android.app.DownloadManager;
import android.database.Cursor;
import android.os.Handler;

import com.google.gson.Gson;
import com.mfamilys.mine.api.Dailyapi;
import com.mfamilys.mine.database.cache.BaseCache;
import com.mfamilys.mine.database.table.DailyTable;
import com.mfamilys.mine.model.daily.DailyBean;
import com.mfamilys.mine.model.daily.StoryBean;
import com.mfamilys.mine.suppost.CONSTANT;
import com.mfamilys.mine.suppost.HttpUtil;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mfamilys on 16-4-8.
 */
public class DailyCache extends BaseCache<StoryBean> {

    public DailyCache(Handler handler){ super(handler);}

    @Override
    protected void putData() {
        db.execSQL(mHelper.DELETE_TABLE_DATA+ DailyTable.NAME);
        for(int i=0;i<mList.size();i++){
            StoryBean storyBean=mList.get(i);
            values.put(DailyTable.TITLE,storyBean.getTitle());
            values.put(DailyTable.ID,storyBean.getId());
            values.put(DailyTable.IMAGE,storyBean.getImages()[0]);
            values.put(DailyTable.BODY,storyBean.getBody());
            values.put(DailyTable.LARGEPIC,storyBean.getLargepic());
            values.put(DailyTable.IS_COLLECTED,storyBean.isCollected());
            db.insert(DailyTable.NAME,null,values);
        }
        db.execSQL(DailyTable.SQL_INIT_COLLECTION_FLAG);
    }

    protected void putData(StoryBean storyBean){
        values.put(DailyTable.TITLE,storyBean.getTitle());
        values.put(DailyTable.ID,storyBean.getId());
        values.put(DailyTable.IMAGE,storyBean.getImages()[0]);
        values.put(DailyTable.BODY,storyBean.getBody()==null?"":storyBean.getBody());
        values.put(DailyTable.LARGEPIC,storyBean.getLargepic());
        db.insert(DailyTable.COLLECTION_NAME,null,values);
    }

    @Override
    public void loadFromCache() {
        mList.clear();
        String sql="select * from "+DailyTable.NAME+" order by "+DailyTable.ID+" desc";

        Cursor cursor=query(sql);
        while(cursor.moveToNext()){
            StoryBean storyBean=new StoryBean();
            storyBean.setTitle(cursor.getString(DailyTable.ID_TITLE));
            storyBean.setId(cursor.getInt(DailyTable.ID_ID));
            storyBean.setImages(new String[]{cursor.getString(DailyTable.ID_IMAGE)});;
            storyBean.setBody(cursor.getString(DailyTable.ID_BODY));
            storyBean.setLargepic(cursor.getString(DailyTable.ID_LARGEPIC));
            storyBean.setCollected(cursor.getInt(DailyTable.ID_IS_COLLECTED));
            //从缓存中取出数据放入mList中
            mList.add(storyBean);
        }
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        cursor.close();
    }

    public void load(){
        Request.Builder builder=new Request.Builder();
        builder.url(Dailyapi.daily_url);
        Request request=builder.build();
        HttpUtil.enqueue(request,new Callback(){
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()==false){
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return;
                }
                String res=response.body().string();
                ArrayList<String> collectionTitles=new ArrayList<String>();
                for(int i=0;i<mList.size();i++){
                    if(mList.get(i).isCollected()==true){
                        collectionTitles.add(mList.get(i).getTitle());
                    }
                }
                List<StoryBean> oldList=new ArrayList<StoryBean>();
                List<StoryBean> newList=new ArrayList<StoryBean>();

                for(StoryBean storyBean:mList){
                    oldList.add(storyBean);
                }
                Gson gson=new Gson();
                DailyBean dailyBean=gson.fromJson(res,DailyBean.class);
                StoryBean[] storyBeens=dailyBean.getStories();
                for(StoryBean storyBean :storyBeens){
                    newList.add(storyBean);
                }
                loadOld(dailyBean.getDate(),oldList,newList);
            }
        });
    }
    private void loadOld(String date,final List<StoryBean> oldList,final List<StoryBean> newList){
        Request.Builder builder=new Request.Builder();
        builder.url(Dailyapi.daily_old_url+date);
        Request request=builder.build();
        HttpUtil.enqueue(request,new Callback(){
            @Override
            public void onFailure(Request request, IOException e) {
                mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()==false){
                    mHandler.sendEmptyMessage(CONSTANT.ID_FAILURE);
                    return;
                }
                String res=response.body().string();
                ArrayList<Integer> collectionIDs=new ArrayList<Integer>();
                for(int i=0;i<oldList.size();i++){
                    if(oldList.get(i).isCollected()==true){
                        collectionIDs.add(oldList.get(i).getId());
                    }
                }

                mList.clear();
                Gson gson=new Gson();
                StoryBean[] storyBeen=(gson.fromJson(res,DailyBean.class)).getStories();
                for(StoryBean storyBean:storyBeen){
                    newList.add(storyBean);
                }
                for (StoryBean storyBean:newList){
                    mList.add(storyBean);
                }
                for(Integer id:collectionIDs){
                    for(int i=0;i<mList.size();i++){
                        if(id.equals(mList.get(i).getId())){
                            mList.get(i).setCollected(1);
                        }
                    }
                }

                mHandler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }
        });
    }
}
