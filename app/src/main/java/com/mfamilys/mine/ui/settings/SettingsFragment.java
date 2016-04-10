package com.mfamilys.mine.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.mfamilys.mine.R;
import com.mfamilys.mine.suppost.CONSTANT;
import com.mfamilys.mine.suppost.Settings;
import com.mfamilys.mine.suppost.Utils;


/**
 * Created by mfamilys on 16-4-9.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,
    Preference.OnPreferenceClickListener{
        private Settings mSettings;
        private Preference mlanguage;
        private Preference mSearch;
        private Preference mSwipeBack;
        private Preference mClearCache;
        private CheckBoxPreference mAutoRefresh;
        private CheckBoxPreference mNightMode;
        private CheckBoxPreference mShakeToReturn;
        private CheckBoxPreference mNoPicMode;
        private CheckBoxPreference mExitConfirm;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        mSettings=Settings.getInstance();

        mlanguage=findPreference(Settings.LANGUAGE);
        mSearch=findPreference(Settings.SEARCH);
        mSwipeBack=findPreference(Settings.SWIPE_BACK);
        mClearCache = findPreference(Settings.CLEAR_CACHE);

        mAutoRefresh = (CheckBoxPreference) findPreference(Settings.AUTO_REFRESH);
        mNightMode = (CheckBoxPreference) findPreference(Settings.NIGHT_MODE);
        mShakeToReturn = (CheckBoxPreference) findPreference(Settings.SHAKE_TO_RETURN);
        mNoPicMode = (CheckBoxPreference) findPreference(Settings.NO_PIC_MODE);
        mExitConfirm = (CheckBoxPreference) findPreference(Settings.EXIT_CONFIRM);


        mlanguage.setSummary(this.getResources().getStringArray(R.array.langs)[Utils.getCurrentLanguage()]);
        mSearch.setSummary(this.getResources().getStringArray(R.array.search)[Settings.searchID]);
        mSwipeBack.setSummary(this.getResources().getStringArray(R.array.swipe_back)[Settings.swipeID]);

        mAutoRefresh.setChecked(Settings.isAutoRefresh);
        mNightMode.setChecked(Settings.isNightMode);
        mShakeToReturn.setChecked(Settings.isShakeMode);
        mExitConfirm.setChecked(Settings.isExitConfirm);
        mNoPicMode.setChecked(Settings.noPicMode);

        mAutoRefresh.setOnPreferenceChangeListener(this);
        mNightMode.setOnPreferenceChangeListener(this);
        mShakeToReturn.setOnPreferenceChangeListener(this);
        mExitConfirm.setOnPreferenceChangeListener(this);
        mNoPicMode.setOnPreferenceChangeListener(this);

        mlanguage.setOnPreferenceClickListener(this);
        mSearch.setOnPreferenceClickListener(this);
        mSwipeBack.setOnPreferenceClickListener(this);
        mClearCache.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        if(preference==mAutoRefresh){
            Settings.isAutoRefresh=Boolean.valueOf(o.toString());
            mSettings.putBoolean(mSettings.AUTO_REFRESH,Settings.isAutoRefresh);
            return true;
        }else if(preference==mNightMode){
            Settings.isNightMode=Boolean.valueOf(o.toString());
            Settings.needRecreate=true;
            mSettings.putBoolean(mSettings.NIGHT_MODE,Settings.isNightMode);
            if(mSettings.isNightMode && Utils.getSysScrennBrightness()> CONSTANT.NIGHT_BRIGHTNESS){
                Utils.setSysScreenBrightness(CONSTANT.NIGHT_BRIGHTNESS);
                Toast.makeText(getActivity().getBaseContext(),"夜间",Toast.LENGTH_SHORT).show();
            }else if(mSettings.isNightMode==false && Utils.getSysScrennBrightness()==CONSTANT.NIGHT_BRIGHTNESS){
                Utils.setSysScreenBrightness(CONSTANT.DAY_BRIGHTNESS);
                Toast.makeText(getActivity().getBaseContext(),"白天",Toast.LENGTH_SHORT).show();
            }
            getActivity().recreate();
            return true;
        }else if(preference==mShakeToReturn){
            Settings.isShakeMode=Boolean.valueOf(o.toString());
            mSettings.putBoolean(mSettings.SHAKE_TO_RETURN,mSettings.isShakeMode);
            return true;
        }else if(preference==mExitConfirm){
            Settings.isExitConfirm=Boolean.valueOf(o.toString());
            mSettings.putBoolean(mSettings.EXIT_CONFIRM,mSettings.isExitConfirm);
            return true;
        }else if(preference==mNoPicMode){
            Settings.noPicMode=Boolean.valueOf(o.toString());
            Settings.needRecreate=true;
            mSettings.putBoolean(mSettings.NO_PIC_MODE,Settings.noPicMode);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference==mlanguage){
            showLangDialog();
        }else if(preference==mClearCache){
            Utils.clearCache();
            Settings.needRecreate=true;
            Snackbar.make(getView(),R.string.text_clear_cache_successful,Snackbar.LENGTH_SHORT).show();
        }else if(preference==mSearch){
            ShowSearchSettingDialog();
        }else if(preference==mSwipeBack){
            showSwipeSettingsDialog();
        }
        return false;
    }

    private void showLangDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.text_language))
                .setSingleChoiceItems(
                        getResources().getStringArray(R.array.langs),Utils.getCurrentLanguage(),
                         new  DialogInterface.OnClickListener(){
                             @Override
                             public void onClick(DialogInterface dialogInterface, int i) {
                                 if(i!= Utils.getCurrentLanguage()){
                                     mSettings.putInt(Settings.LANGUAGE,i);
                                     Settings.needRecreate=true;
                                 }
                                 dialogInterface.dismiss();
                                 if(Settings.needRecreate){
                                     getActivity().recreate();
                                 }
                             }
                         }
                ).show();
    }
    private void ShowSearchSettingDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.text_search))
                .setSingleChoiceItems(
                        getResources().getStringArray(R.array.search), Settings.searchID,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Settings.searchID=i;
                                mSettings.putInt(Settings.SEARCH,i);
                                mSearch.setSummary(getResources().getStringArray(R.array.search)[Settings.searchID]);
                                dialogInterface.dismiss();
                            }
                        }
                ).show();
    }
    private void showSwipeSettingsDialog(){
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.text_swipe_to_return))
                .setSingleChoiceItems(
                        getResources().getStringArray(R.array.swipe_back), Settings.swipeID,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("SearchID",Settings.swipeID+"");
                                Log.d("which",i+"");
                                dialogInterface.dismiss();
                                if(i!=Settings.swipeID){
                                    Settings.swipeID=i;
                                    mSettings.putInt(Settings.SWIPE_BACK,i);
                                    mSearch.setSummary(getResources().getStringArray(R.array.swipe_back)[Settings.swipeID]);
                                    getActivity().recreate();
                                }
                            }
                        }
                ).show();
    }
}

