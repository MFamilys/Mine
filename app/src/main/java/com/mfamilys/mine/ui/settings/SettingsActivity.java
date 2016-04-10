package com.mfamilys.mine.ui.settings;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;


import com.mfamilys.mine.R;
import com.mfamilys.mine.suppost.CONSTANT;
import com.mfamilys.mine.suppost.Settings;
import com.mfamilys.mine.suppost.Utils;
import com.mfamilys.mine.ui.support.SwipeBackActivity;

/**
 * Created by mfamilys on 16-4-9.
 */
public class SettingsActivity extends SwipeBackActivity implements SensorEventListener{
    private Toolbar toolbar;
    private int mlang=-1;
    private SensorManager mSenSorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mlang= Utils.getCurrentLanguage();
        if(mlang>-1){
            Utils.ChangeLanguage(this,mlang);
        }
        if(Settings.isNightMode){
            this.setTheme(R.style.NightTheme);
        }else{
            this.setTheme(R.style.DayTheme);
        }
        Settings.swipeID=Settings.getInstance().getInt(Settings.SWIPE_BACK,0);
        setContentView(R.layout.activity_settings);

        mSenSorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.setting);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,new SettingsFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSenSorManager.registerListener(this,mSenSorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        mSenSorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        mSenSorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(Settings.isShakeMode==false){
            return;
        }
        float value[]=sensorEvent.values;
        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            if((Math.abs(value[0]) + Math.abs(value[1]) + Math.abs(value[2]))> CONSTANT.shakeValue){
                onBackPressed();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
