package com.mfamilys.mine;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;

/**
 * Created by mfamilys on 16-4-7.
 */
public class MineApplication extends Application {
    public static Context Appcontext=null;

    @Override
    public void onCreate() {
        super.onCreate();
        //进行全局的上下文的保存
        Appcontext=getApplicationContext();
        //进行类库的初始化
        Fresco.initialize(Appcontext);
        Stetho.initializeWithDefaults(Appcontext);
    }
}

