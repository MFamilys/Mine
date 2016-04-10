package com.mfamilys.mine.ui.daily;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.mfamilys.mine.R;
import com.mfamilys.mine.api.Dailyapi;
import com.mfamilys.mine.database.cache.cache.DailyCache;
import com.mfamilys.mine.database.table.DailyTable;
import com.mfamilys.mine.model.daily.DailyDetailsBean;
import com.mfamilys.mine.model.daily.StoryBean;
import com.mfamilys.mine.suppost.CONSTANT;
import com.mfamilys.mine.suppost.DisplayUtil;
import com.mfamilys.mine.suppost.HttpUtil;
import com.mfamilys.mine.suppost.Settings;
import com.mfamilys.mine.suppost.Utils;
import com.mfamilys.mine.ui.support.BaseDetailsActivity;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class DailyDetailsActivity extends BaseDetailsActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private boolean isShakeMode=false;
    private DailyDetailsBean dailyDetailsBean;
    private String url;
    private int id;
    private String imageUrl;
    private String title;
    private String body;
    private DailyCache cache;
    private StoryBean storyBean;


    @Override
    protected void onDataRefresh() {
        Request.Builder builder=new Request.Builder();
        builder.url(url);
        Request request=builder.build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(CONSTANT.ID_FAILURE);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String res=response.body().string();
                Utils.Dlog(res);
                Gson gson=new Gson();
                dailyDetailsBean=gson.fromJson(res,DailyDetailsBean.class);
                cache.execSQL(DailyTable.updateBodyContent(DailyTable.NAME,dailyDetailsBean.getTitle(),dailyDetailsBean.getBody()));
                cache.execSQL(DailyTable.updateBodyContent(DailyTable.COLLECTION_NAME,dailyDetailsBean.getTitle(),dailyDetailsBean.getBody()));
                cache.execSQL(DailyTable.updateLargePic(DailyTable.NAME,dailyDetailsBean.getTitle(),dailyDetailsBean.getImage()));
                cache.execSQL(DailyTable.updateLargePic(DailyTable.COLLECTION_NAME,dailyDetailsBean.getTitle(),dailyDetailsBean.getImage()));
                imageUrl=dailyDetailsBean.getImage();
                body=dailyDetailsBean.getBody();
                handler.sendEmptyMessage(CONSTANT.ID_SUCCESS);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        initView();
        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);

    }
    private void getData(){
        storyBean=new StoryBean();
        url=getIntent().getStringExtra(getString(R.string.id_url));
        id = getIntent().getIntExtra(getString(R.string.id_id),0);
        body = getIntent().getStringExtra(getString(R.string.id_body));
        title = getIntent().getStringExtra(getString(R.string.id_title));
        imageUrl = getIntent().getStringExtra(getString(R.string.id_imageurl));
        isCollected = getIntent().getBooleanExtra(getString(R.string.id_collection),false);

        storyBean.setId(id);
        storyBean.setBody(body);
        storyBean.setTitle(title);
        storyBean.setLargepic(imageUrl);
        storyBean.setImages(new String[]{getIntent().getStringExtra(getString(R.string.id_small_image))});
    }
    protected void initView(){
        super.initView();
        cache = new DailyCache(handler);
        if(body == "" || body == null||imageUrl == null || imageUrl == "") {
            onDataRefresh();
        }else{
            handler.sendEmptyMessage(CONSTANT.ID_FROM_CACHE);
        }
    }

    Handler handler=new Handler(new Handler.Callback(){
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case CONSTANT.ID_FAILURE:
                    hideLoading();
                    displayNetworkError();
                    break;
                case CONSTANT.ID_SUCCESS:
                case CONSTANT.ID_FROM_CACHE:
                    if(HttpUtil.isWiFi==true|| Settings.getInstance().getBoolean(Settings.NO_PIC_MODE,false)==false){
                        setMainContentBg(imageUrl);
                    }
                    scrollView.setVisibility(View.VISIBLE);
                    scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            topImage.setTranslationY(Math.max(-scrollY/2,-DisplayUtil.dip2px(getBaseContext(),170)));
                        }
                    });
                    contentView.loadDataWithBaseURL("file:///android_asset/", "<link rel=\"stylesheet\" type=\"text/css\" href=\"dailycss.css\" />"+body, "text/html", "utf-8", null);
                    hideLoading();
                    break;
            }
            return false;
        }

    });

    @Override
    protected void removeFromCollection() {
        cache.execSQL(DailyTable.updateCollectionFlag(storyBean.getTitle(),0));
        cache.execSQL(DailyTable.deleteCollectionFlag(storyBean.getTitle()));
    }

    @Override
    protected void addToCollection() {
        cache.execSQL(DailyTable.updateCollectionFlag(storyBean.getTitle(),1));
        cache.addToCollection(storyBean);
    }

    @Override
    protected String getShareInfo() {
        return "["+title+"]:"+ Dailyapi.daily_story_base_url+id+" ("+getString(R.string.text_share_from)+getString(R.string.app_name)+")";
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        isShakeMode=Settings.getInstance().getBoolean(Settings.SHAKE_TO_RETURN,true);
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(isShakeMode==false){
            return;
        }
        float value[]= sensorEvent.values;
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            if((Math.abs(value[0])+Math.abs(value[1])+Math.abs(value[2]))>CONSTANT.shakeValue){
                onBackPressed();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
