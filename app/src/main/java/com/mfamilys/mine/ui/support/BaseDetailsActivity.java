package com.mfamilys.mine.ui.support;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mfamilys.mine.MineApplication;
import com.mfamilys.mine.R;
import com.mfamilys.mine.suppost.DisplayUtil;
import com.mfamilys.mine.suppost.HttpUtil;
import com.mfamilys.mine.suppost.ImageUtil;
import com.mfamilys.mine.suppost.Settings;
import com.mfamilys.mine.suppost.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Method;

import butterknife.Bind;
import butterknife.ButterKnife;

public abstract class BaseDetailsActivity extends SwipeBackActivity {

    @Bind(R.id.progressBarTopPic)
    protected ProgressBar progressBarTopPic;
    @Bind(R.id.topImage)
    protected SimpleDraweeView topImage;
    @Bind(R.id.content_view)
    protected  WebView contentView;
    @Bind(R.id.scrollView)
    protected NestedScrollView scrollView;
    @Bind(R.id.progressBar)
    protected ProgressBar progressBar;
    @Bind(R.id.networkBtn)
    protected ImageButton networkBtn;
    @Bind(R.id.stbar)
    protected  LinearLayout stbar;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.toolbarcontent)
    protected  FrameLayout toolbarcontent;
    @Bind(R.id.main_content)
    protected FrameLayout mainContent;

    protected boolean isCollected;
    private int mLang=-1;
    protected abstract void onDataRefresh();
    protected abstract void removeFromCollection();
    protected abstract void addToCollection();
    protected abstract String getShareInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //如果是4.4以上的系统,则设置为translucent状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(savedInstanceState);
        loadSettings();
        setContentView(getLayoutID());
        ButterKnife.bind(this);

    }
    protected  void loadSettings(){
        mLang= Utils.getCurrentLanguage();
        if(mLang>-1){
            Utils.ChangeLanguage(this,mLang);
        }
        if(Settings.isNightMode){
            this.setTheme(R.style.NightTheme);
        }else{
            this.setTheme(R.style.DayTheme);
        }
    }
    protected  int getLayoutID(){return R.layout.activity_base_details;}
    public void displayLoading(){
        if(progressBar != null){
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    public void hideLoading() {
        if(progressBar != null){
            progressBar.setVisibility(View.GONE);
            progressBarTopPic.setVisibility(View.VISIBLE);
        }
    }

    public void displayNetworkError() {
        if(networkBtn != null){
            networkBtn.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 设置布局背景，颜色取自顶部图片的主色调
     **/
    protected  void setMainContentBg(final String url){
        if(Utils.hasString(url)==false){
            setDefaultColor();
            return;
        }
        Request.Builder builder=new Request.Builder();
        builder.url(url);
        Request request=builder.build();
        HttpUtil.enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        setDefaultColor();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(bitmap == null){
                            setDefaultColor();
                            return;
                        }
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                            topImage.setBackground(new BitmapDrawable(getResources(), bitmap));
                        } else{
                            topImage.setImageURI(Uri.parse(url));
                        }
                        mainContent.setBackgroundColor(ImageUtil.getImageColor(bitmap));
                        progressBarTopPic.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
    protected void setDefaultColor(){
        mainContent.setBackgroundColor(Color.rgb(67,76,66));
        progressBarTopPic.setVisibility(View.GONE);
    }
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_share,menu);
        updateCollectionMenu(menu.findItem(R.id.menu_collect));
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_share) {
            //分享信息
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getShareInfo());
            startActivity(Intent.createChooser(sharingIntent,getString(R.string.hint_share_to)));
            return super.onOptionsItemSelected(item);

        }else if(item.getItemId() == R.id.menu_collect){
            //收藏标志
            if(isCollected){
                removeFromCollection();
                isCollected = false;
                updateCollectionMenu(item);
                Snackbar.make(mainContent, R.string.notify_remove_from_collection,Snackbar.LENGTH_SHORT).show();
            }else {
                addToCollection();
                isCollected = true;
                updateCollectionMenu(item);
                Snackbar.make(mainContent, R.string.notify_add_to_collection,Snackbar.LENGTH_SHORT).show();

            }
        }
        return true;
    }
    protected void updateCollectionMenu(MenuItem item){
        if(isCollected){
            item.setIcon(R.mipmap.ic_star_black);
        }else {
            item.setIcon(R.mipmap.ic_star_white);
        }
    }

    protected void initView() {
        /**
         * TODO 测试
         */

        //对toolbar进行下移
        int height = DisplayUtil.getScreenHeight(MineApplication.Appcontext);
        LinearLayout ll = (LinearLayout) findViewById(R.id.stbar);
        LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) ll.getLayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            llp.height = (int) (height * 0.03);
            ll.setLayoutParams(llp);
        }

        mainContent = (FrameLayout) findViewById(R.id.main_content);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarTopPic=(ProgressBar) findViewById(R.id.progressBarTopPic);
        networkBtn = (ImageButton) findViewById(R.id.networkBtn);
        topImage = (SimpleDraweeView) findViewById(R.id.topImage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.top_gradient));

        contentView = (WebView) findViewById(R.id.content_view);
        //允许JS脚本运行
        contentView.getSettings().setJavaScriptEnabled(true);

        contentView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                displayNetworkError();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                displayNetworkError();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                contentView.loadUrl(url);
                return false;
            }
        });

        /*
         *    cache web page 开启webView中网页的缓存
         */

        contentView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        contentView.getSettings().setDomStorageEnabled(true);
        contentView.getSettings().setDatabaseEnabled(true);


        if(HttpUtil.isWiFi == false) {
            //在不是WiFI下,并且无图模式有效,则取消图片显示
            contentView.getSettings().setBlockNetworkImage(Settings.getInstance().getBoolean(Settings.NO_PIC_MODE, false));
        }else {
            contentView.getSettings().setBlockNetworkImage(false);
        }



        /**
         * 网络异常就显示
         */
        networkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                onDataRefresh();
            }
        });
    }
}
