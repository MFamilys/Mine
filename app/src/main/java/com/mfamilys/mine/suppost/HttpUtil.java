package com.mfamilys.mine.suppost;

import android.content.Context;
import android.net.ConnectivityManager;

import com.mfamilys.mine.MineApplication;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by mfamilys on 16-4-8.
 */
public class HttpUtil {
    private static final OkHttpClient mOkHttpClient=new OkHttpClient();
    public static boolean isWiFi=true;
    static{
        mOkHttpClient.setConnectTimeout(30,TimeUnit.SECONDS);
        mOkHttpClient.setConnectTimeout(30,TimeUnit.SECONDS);
        mOkHttpClient.setConnectTimeout(30,TimeUnit.SECONDS);
    }
    /**
     * 直接访问,但不会开启异步线程
     */
    public static Response execute(Request request) throws IOException{
        return mOkHttpClient.newCall(request).execute();
    }
    /**
    * 开启异步线程访问网络
    * @param request
    * @param responseCallback
    * */
    public static void execute(Request request, Callback responseCallback){
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }
    public static void enqueue(Request request,Callback response){
        mOkHttpClient.newCall(request).enqueue(response);
    }
    public static void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }
    public static boolean readNetworkState(){
        ConnectivityManager cm =(ConnectivityManager) MineApplication.Appcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm!=null && cm.getActiveNetworkInfo()!=null && cm.getActiveNetworkInfo().isConnected()){
            isWiFi=(cm.getActiveNetworkInfo().getType()==ConnectivityManager.TYPE_WIFI);
            return true;
        }else{
            return false;
        }
    }
}
