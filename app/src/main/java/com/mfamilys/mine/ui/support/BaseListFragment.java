package com.mfamilys.mine.ui.support;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mfamilys.mine.MineApplication;
import com.mfamilys.mine.R;
import com.mfamilys.mine.database.cache.ICache;
import com.mfamilys.mine.suppost.CONSTANT;
import com.mfamilys.mine.suppost.HttpUtil;
import com.mfamilys.mine.suppost.Settings;
import com.mfamilys.mine.suppost.Utils;
import com.yalantis.phoenix.PullToRefreshView;

/**
 * Created by mfamilys on 16-4-8.
 */
public abstract class BaseListFragment extends Fragment {

    protected View parentView;
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    //下拉刷新按钮
    protected PullToRefreshView refreshView;

    protected ImageView placeHolder;
    //进度条
    protected ProgressBar progressBar;
    //循环组件的适配器
    protected RecyclerView.Adapter adapter;

    protected int mLayout=0;
    protected ICache cache;

    protected boolean withHeaderTab = true;
    protected boolean withRefreshView = true;
    protected boolean needCache = true;

    protected  abstract void onCreateCache();
    protected  abstract RecyclerView.Adapter bindAdapter();
    protected abstract void loadFromNet();
    protected abstract void loadFromCache();
    protected abstract boolean hasData();
    protected abstract void getArgs();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //进行初始化
        setLayout();
        needCache=true;
        getArgs();
        //获取组件
        parentView=inflater.inflate(mLayout,container,false);
        withHeaderTab=setHeaderTab();
        withRefreshView=setRefreshView();
        progressBar = (ProgressBar) parentView.findViewById(R.id.progressbar);
        recyclerView = (RecyclerView) parentView.findViewById(R.id.recycleView);
        placeHolder = (ImageView) parentView.findViewById(R.id.placeholder);
        refreshView = (PullToRefreshView) parentView.findViewById(R.id.pull_to_refresh);

        onCreateCache();

        adapter=bindAdapter();

        mLayoutManager=new LinearLayoutManager(MineApplication.Appcontext);

        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);

        View view=getActivity().findViewById(R.id.tab_layout);
        if(withHeaderTab){
            view.setVisibility(View.VISIBLE);
        }else{
            if(view !=null){
                view.setVisibility(View.GONE);
            }
        }
        if(withRefreshView){
            refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener(){
                @Override
                public void onRefresh() {
                    loadFromNet();
                }
            });
            placeHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeHolder.setVisibility(View.GONE);
                    loadFromNet();
                }
            });
        }else{
            refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener(){
                @Override
                public void onRefresh() {
                    refreshView.setRefreshing(false);
                }
            });
        }
        HttpUtil.readNetworkState();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadFromCache();
            }
        }).start();
        return parentView;
    }

    protected boolean setHeaderTab(){
        return true;
    }
    protected boolean setRefreshView(){
        return true;
    }
    protected boolean setCache(){
        return true;
    }
    protected void setLayout(){
        mLayout = R.layout.layout_common_list;
    }

    protected Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case CONSTANT.ID_FAILURE:
                    if(isAdded()) {
                        Utils.Dlog(getString(R.string.Text_Net_Exception));
                    }
                    break;
                case CONSTANT.ID_SUCCESS:
                    if(isAdded()){
                        Utils.Dlog(getString(R.string.text_refresh_success));
                    }
                    if(needCache){
                        //加载成功后进行缓存
                        cache.cache();
                    }
                    break;
                case CONSTANT.ID_FROM_CACHE:
                    if(withRefreshView && hasData() == false){
                        //需要刷新并且不存在数据
                        loadFromNet();
                        return false;
                    }else if(Settings.isAutoRefresh && HttpUtil.isWiFi){
                        //WiFi环境下
                        loadFromNet();
                        return false;
                    }
                    break;
            }
            if(withRefreshView){
                refreshView.setRefreshing(false);
            }
            if(hasData()){
                placeHolder.setVisibility(View.GONE);
            }else{
                placeHolder.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
            return false;
        }
    });
}
