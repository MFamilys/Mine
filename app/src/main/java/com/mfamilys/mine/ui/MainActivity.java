package com.mfamilys.mine.ui;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.mfamilys.mine.R;
import com.mfamilys.mine.suppost.CONSTANT;
import com.mfamilys.mine.suppost.Settings;
import com.mfamilys.mine.suppost.Utils;
import com.mfamilys.mine.ui.collection.BaseCollectionFragment;
import com.mfamilys.mine.ui.daily.DailyFragment;
import com.mfamilys.mine.ui.settings.SettingsActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mfamilys on 16-4-7.
 */
public class MainActivity extends AppCompatActivity implements
        SensorEventListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tab_layout)
    SmartTabLayout tabLayout;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;

    //实现了重力传感器接口，来实现摇晃退出功能
    private Drawer drawer;
    //账号头像
    private AccountHeader header;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction fragmentTransaction;
    private Fragment currentFragment;
    private Menu menu;
    private SensorManager mSensorManeger;
    //语言标记
    private int mlang = -1;
    //传感器标记
    private boolean isShake = false;
    //设置时间戳来实现连续按键是否可以退出
    private long lastPressTime = 0;
    //设置对象
    private Settings mSettings = Settings.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载设置
        loadSetting();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        mSensorManeger=(SensorManager)getSystemService(SENSOR_SERVICE);
        currentFragment=new DailyFragment();
        switchFragment(currentFragment,getString(R.string.daily),R.menu.menu_daily);
        Utils.Dlog("------version: "+Utils.getVersion());
    }

    public void loadSetting() {
        Settings.isShakeMode = mSettings.getBoolean(Settings.SHAKE_TO_RETURN, false);
        Settings.searchID = mSettings.getInt(Settings.SEARCH, 0);
        Settings.swipeID = mSettings.getInt(Settings.SWIPE_BACK, 0);
        Settings.isAutoRefresh = mSettings.getBoolean(Settings.AUTO_REFRESH, false);
        Settings.isExitConfirm = mSettings.getBoolean(Settings.EXIT_CONFIRM, true);
        Settings.isNightMode = mSettings.getBoolean(Settings.NIGHT_MODE, false);
        Settings.noPicMode = mSettings.getBoolean(Settings.NO_PIC_MODE, false);
        //切换夜间模式
        if (mSettings.isNightMode && Utils.getSysScrennBrightness() > CONSTANT.NIGHT_BRIGHTNESS) {
            Utils.setSysScreenBrightness(CONSTANT.NIGHT_BRIGHTNESS);
        } else if (mSettings.isNightMode == false && Utils.getSysScrennBrightness() == CONSTANT.NIGHT_BRIGHTNESS) {
            Utils.setSysScreenBrightness(CONSTANT.DAY_BRIGHTNESS);
        }
        if (Settings.isNightMode) {
            this.setTheme(R.style.NightTheme);
        } else {
            this.setTheme(R.style.DayTheme);
        }
        //转换语言
        mlang = Utils.getCurrentLanguage();
        if (mlang > -1) {
            Utils.ChangeLanguage(this, mlang);
        }
    }



    private void initData(){
        setSupportActionBar(toolbar);
        //账号头像设置
        header=new AccountHeaderBuilder().withActivity(this)
                .withCompactStyle(false)
                .withHeaderBackground(R.drawable.background)
                .addProfiles(new ProfileDrawerItem()
                        .withIcon(R.drawable.header)
                        .withEmail(getString(R.string.author_email))
                        .withName(getString(R.string.author_name))
                ).withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //TODO
                       // Intent i=new Intent(MainActivity.this,MainActivity.class);
                       // startActivity(i);
                        return  false;
                    }
                }).build();
                //抽屉设置
        drawer=new DrawerBuilder().withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(header)
                .withSliderBackgroundColor(Settings.isNightMode ? ContextCompat.getColor(this,R.color.night_primary):ContextCompat.getColor(this,R.color.white))
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.daily).withIcon(R.mipmap.ic_home).withIdentifier(R.mipmap.ic_home)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new PrimaryDrawerItem().withName(R.string.collection).withIcon(R.mipmap.ic_collect_grey).withIdentifier(R.mipmap.ic_collect_grey)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new SectionDrawerItem().withName(R.string.app_name).withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_color)),
                        new SecondaryDrawerItem().withName(Settings.isNightMode == true ? R.string.text_day_mode: R.string.text_night_mode)
                                .withIcon(Settings.isNightMode == true ? R.mipmap.ic_day_white:R.mipmap.ic_night).withIdentifier(R.mipmap.ic_night)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white) : ContextCompat.getColor(this, R.color.text_light)),
                        new SecondaryDrawerItem().withName(R.string.setting).withIcon(R.mipmap.ic_setting).withIdentifier(R.mipmap.ic_setting)
                                .withTextColor(Settings.isNightMode ? ContextCompat.getColor(this, R.color.white):ContextCompat.getColor(this,R.color.text_light))
                )
                .withOnDrawerItemClickListener(
                        new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                switch (drawerItem.getIdentifier()) {
                                    case R.mipmap.ic_home:
                                        if (currentFragment instanceof DailyFragment) {
                                            return false;
                                        }
                                        currentFragment = new DailyFragment();
                                        break;
                                    case R.mipmap.ic_collect_grey:
                                        if (currentFragment instanceof BaseCollectionFragment) {
                                            return false;
                                        }
                                        currentFragment = new BaseCollectionFragment();
                                        break;
                                    case R.mipmap.ic_night:
                                        Settings.isNightMode = !Settings.isNightMode;
                                        mSettings.putBoolean(mSettings.NIGHT_MODE, Settings.isNightMode);
                                        MainActivity.this.recreate();
                                        return false;
                                    case R.mipmap.ic_setting:
                                        Intent toSetting = new Intent(MainActivity.this, SettingsActivity.class);
                                        toSetting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(toSetting);
                                        return false;

                                }
                                switchFragment();
                                return false;

                            }
                        }
                )
                .build();
    }

    private void switchFragment() {
        if (currentFragment instanceof DailyFragment) {
            switchFragment(currentFragment, getString(R.string.daily), R.menu.menu_daily);
        }
        else if(currentFragment instanceof BaseCollectionFragment){
            switchFragment(currentFragment,getString(R.string.collection),R.menu.menu_daily);
        }
    }

    private void switchFragment(Fragment fragment,String title,int resourceMenu){
        //利用传入的Fragment替代当前的fragment布局
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment).commit();
        //替换标题
        getSupportActionBar().setTitle(title);
        //替换菜单
        if(menu!=null){
            menu.clear();
            getMenuInflater().inflate(resourceMenu,menu);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu=menu;
        getMenuInflater().inflate(R.menu.menu_daily,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen()){
            drawer.closeDrawer();
        }else if(isShake==false && canExit()){
            super.onBackPressed();
        }
        isShake=false;
    }
    private boolean canExit(){
        if(Settings.isExitConfirm){
            //两次按键之间间隔不小于2秒
            if(System.currentTimeMillis() - lastPressTime > CONSTANT.exitConfirmTime){
                lastPressTime = System.currentTimeMillis();
                //底端提示栏
                Snackbar.make(getCurrentFocus(), R.string.notify_exit_confirm,Snackbar.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManeger.registerListener(this,mSensorManeger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        SensorManager.SENSOR_DELAY_NORMAL);
        if(Settings.needRecreate){
            Settings.needRecreate=false;
            this.recreate();
        }
    }

    @Override
    protected void onStop() {
        mSensorManeger.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        mSensorManeger.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(Settings.isShakeMode==false){
            return;
        }
        float value[]=sensorEvent.values;
        if(sensorEvent.sensor.getType()== Sensor.TYPE_ACCELEROMETER){
            if((Math.abs(value[0])+Math.abs(value[1])+Math.abs(value[2]))>CONSTANT.shakeValue){
                isShake=true;
                onBackPressed();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
