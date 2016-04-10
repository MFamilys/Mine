package com.mfamilys.mine.database.cache;

import java.util.List;

/**
 * Created by mfamilys on 16-4-8.
 */
public interface ICache<T> {
    //缓存的接口,指定缓存必须实现的方法
    void addToCollection(T object);
    void execSQL(String sql);
    List<T> getmList();
    boolean hasData();
    void load();
    void loadFromCache();
    void cache();
}
