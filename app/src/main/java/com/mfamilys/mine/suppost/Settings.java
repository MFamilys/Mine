package com.mfamilys.mine.suppost;

import android.content.Context;
import android.content.SharedPreferences;

import com.mfamilys.mine.MineApplication;

/**
 * Created by mfamilys on 16-4-7.
 */
public class Settings {
    //是否需要重新创建
    public static boolean needRecreate =false;
    //是否支持摇晃退出
    public static boolean isShakeMode =true;
    //是否支持无图模式
    public static boolean noPicMode =false;
    //是否是夜间模式
    public static boolean isNightMode = false;
    //是否在wifi环境下自动刷新
    public static boolean isAutoRefresh =false;
    //是否按两次退出键才退出
    public static boolean isExitConfirm =true;
    //需要查找的ID
    public static int searchID=0;
    public static int swipeID=0;

    public static final String XML_NAME="settings";

    public static final String SHAKE_TO_RETURN="shake_to_return_new";

    public static final String NO_PIC_MODE="no_pic_mode";

    public static final String NIGHT_MODE="night_mode";

    public static final String AUTO_REFRESH="auto_refresh";

    public static final String LANGUAGE ="language";

    public static final String EXIT_CONFIRM="exit_confirm";

    public static final String CLEAR_CACHE="clear_cache";

    public static final String SEARCH="search";

    public static final String SWIPE_BACK="swipe_back";

    private static Settings sInstance;
    //利用SharedPreference来对设置进行缓存
    private SharedPreferences mPrefs;

    public static Settings getInstance(){
        if(sInstance==null){
            sInstance=new Settings(MineApplication.Appcontext);
        }
        return sInstance;
    }
    private Settings(Context context){
        mPrefs= context.getSharedPreferences(XML_NAME,Context.MODE_PRIVATE);
    }
    //将SharedPreference的存取进行二次封装
    public Settings putBoolean(String key, boolean value) {
        mPrefs.edit().putBoolean(key, value).commit();
        return this;
    }

    public boolean getBoolean(String key, boolean def) {
        return mPrefs.getBoolean(key, def);
    }

    public Settings putInt(String key, int value) {
        mPrefs.edit().putInt(key, value).commit();
        return this;
    }

    public int getInt(String key, int defValue) {
        return mPrefs.getInt(key, defValue);
    }

    public Settings putString(String key, String value) {
        mPrefs.edit().putString(key, value).commit();
        return this;
    }

    public String getString(String key, String defValue) {
        return mPrefs.getString(key, defValue);
    }

}
