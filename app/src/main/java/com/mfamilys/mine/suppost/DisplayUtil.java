package com.mfamilys.mine.suppost;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by mfamilys on 16-4-9.
 */
public class DisplayUtil {
    /**
     * 根据手机分辨率从dp的单位转换成px
    */
    public static int dip2px(Context context,float dpValue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }
    public static int px2dip(Context context,float pxVlaue){
        final float scale=context.getResources().getDisplayMetrics().density;
        return (int)(pxVlaue/scale+0.5f);
    }

    private static int screenWidth=-1;
    private static int screenHeight=-1;
    public static int getScreenWidth(Context context){
        if(screenWidth!=-1){
            return screenWidth;
        }
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm =new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenWidth=dm.widthPixels;
        return screenWidth;
    }
    public static int getScreenHeight(Context context){
        if(screenHeight!=-1){
            return screenHeight;
        }
        WindowManager wm=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        screenHeight=dm.heightPixels;
        return screenHeight;
    }
    public static void Snack(View view, String str){
        Snackbar.make(view,str,Snackbar.LENGTH_SHORT).show();
    }
}
