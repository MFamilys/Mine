package com.mfamilys.mine.database.cache.collection;

import android.database.Cursor;
import android.os.Handler;

import com.mfamilys.mine.database.cache.BaseCollectionCache;
import com.mfamilys.mine.database.table.DailyTable;
import com.mfamilys.mine.model.daily.DailyBean;
import com.mfamilys.mine.model.daily.StoryBean;
import com.mfamilys.mine.suppost.CONSTANT;

/**
 * Created by mfamilys on 16-4-8.
 */
public class CollectionDailyCache extends BaseCollectionCache<StoryBean> {
    private DailyTable table;
    public CollectionDailyCache(Handler mHandler){super(mHandler);}

    @Override
    public void loadFromCache() {
        Cursor cursor=query(table.SELECT_ALL_FROM_COLLECTION);
        while(cursor.moveToNext()){
            StoryBean storyBean=new StoryBean();
            storyBean.setTitle(cursor.getString(table.ID_TITLE));
            storyBean.setId(cursor.getInt(table.ID_ID));
            storyBean.setImages(new String[]{cursor.getString(table.ID_IMAGE)});
            storyBean.setBody(cursor.getString(table.ID_BODY));
            storyBean.setLargepic(cursor.getString(table.ID_LARGEPIC));
            mList.add(storyBean);
        }
        mHandler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
    }
}
