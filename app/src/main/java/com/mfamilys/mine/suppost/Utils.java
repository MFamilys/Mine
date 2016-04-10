package com.mfamilys.mine.suppost;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.mfamilys.mine.MineApplication;
import com.mfamilys.mine.R;
import com.mfamilys.mine.database.DatabaseHelper;
import com.mfamilys.mine.database.table.DailyTable;
import com.mfamilys.mine.suppost.adapter.DailyAdapter;

import java.io.InputStream;
import java.util.Locale;

/**
 * Created by mfamilys on 16-4-7.
 */
public class Utils {
    //是否为调试模式
    private static boolean DEBUG=true;
    //全局上下文
    private static Context mContext= MineApplication.Appcontext;


    //调试的二次封装方法,当debug为0的时候不会显示
    public static void Dlog(String text){
        if(DEBUG){
            Log.d(mContext.getString(R.string.text_debug_data),text);

        }
    }

    //获取当前语言
    public static int getCurrentLanguage(){
        int lang=Settings.getInstance().getInt(Settings.LANGUAGE,-1);
        //中文-汉语为1,中文-其他为2,外语为0
        if(lang == -1){
            String language = Locale.getDefault().getLanguage();
            String country=Locale.getDefault().getCountry();
            if(language.equalsIgnoreCase("zh")){
                if(country.equalsIgnoreCase("CN")){
                    lang=1;
                }else{
                    lang=2;
                }
            }else{
                lang = 0;
            }
        }
        return lang;
    }
    //改变语言
    public static void ChangeLanguage(Context context,int lang){
        String language=null;
        String country=null;
        switch(lang){
            case 1:
                language="zh";
                country="CN";
                break;
            case 2:
                language="zh";
                country="TW";
                break;
            default:
                language="en";
                country="US";
                break;
        }
        Locale locale=new Locale(language,country);
        Configuration conf=context.getResources().getConfiguration();
        conf.locale=locale;
        context.getApplicationContext().getResources().updateConfiguration(conf,context.getResources().getDisplayMetrics());
    }
    /*
    *   获取系统当前的亮度值：0-255
    */
    public static final int MAX_BRIGHTNESS=255;
    public static int getSysScrennBrightness(){
        int screenBrightness=MAX_BRIGHTNESS;
        try{
            //利用ContentProvider来获取系统当前亮度
            screenBrightness=android.provider.Settings.System.getInt(
                    mContext.getContentResolver(),android.provider.Settings.System.SCREEN_BRIGHTNESS);
        }catch(Exception e){
            Utils.Dlog("获取系统当前的亮度值失败");
        }
        return screenBrightness;
    }
    /*
    *   设置系统当前的亮度值：0-255
    */
    public static void setSysScreenBrightness(int brightness){
        try{
            ContentResolver resolver=mContext.getContentResolver();
            //通过系统获取改变系统路径的uri
            Uri uri=android.provider.Settings.System.getUriFor(android.provider.Settings.System.SCREEN_BRIGHTNESS);
            //通过ContentProvider来设置亮度值
            android.provider.Settings.System.putInt(resolver,android.provider.Settings.System.SCREEN_BRIGHTNESS,brightness);
            resolver.notifyChange(uri,null);//实时通知系统进行改变
        }catch(Exception e){
            Utils.Dlog("设置系统当前的亮度值失败: "+e);
        }
    }
    //判断是否为空
    public static boolean hasString(String str){
        if(str == null || str.equals("")) return false;
        return true;
    }

    public static String getVersion() {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void clearCache(){
        WebView wb=new WebView(mContext);
        wb.clearCache(true);
        DatabaseHelper mHelper=DatabaseHelper.instance(mContext);
        SQLiteDatabase db=mHelper.getWritableDatabase();
        db.execSQL(mHelper.DELETE_TABLE_DATA+ DailyTable.NAME);
    }
}
