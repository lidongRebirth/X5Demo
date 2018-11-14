package com.myfittinglife.app.x5demo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者    LD
 * 修改
 * 时间    2018.11.13 17:15
 * 描述    系统web
 */
public class NativeWebActivity extends AppCompatActivity {

    @BindView(R.id.native_web)
    WebView nativeWeb;
    @BindView(R.id.ib_back)             //返回键
    ImageButton ibBack;
    @BindView(R.id.tv_middle)           //title
    TextView tvMiddle;

    private String url_iqiyi = "http://iqiyi.com";
    private String url_QQYinYue = "https://y.qq.com/n/yqq/playsquare/2961613734.html#stat=y_new.index.playlist.pic";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_web);
        ButterKnife.bind(this);
        initWebViewSettings();
        nativeWeb.loadUrl(url_iqiyi);




        nativeWeb.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {//这个方法是在5.0以下使用的
                view.loadUrl(String.valueOf(request.getUrl()));
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {//这个方法是在5.0以上的版本上使用的
                view.loadUrl(url);
                return true;
            }

        });
        nativeWeb.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {       //设置自己的标题栏
                super.onReceivedTitle(view, title);
                if(title!=null){
                    tvMiddle.setText(title);
                }
            }

        });


    }

    @OnClick(R.id.ib_back)
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.ib_back:      //返回按钮
                finish();
        }
    }
    @Override
    protected void onResume() {
//        nativeWeb.onResume();
//        nativeWeb.resumeTimers();
        super.onResume();
    }

    @Override
    protected void onPause() {
//        nativeWeb.onPause();
//        nativeWeb.pauseTimers();
        super.onPause();
    }


    private void initWebViewSettings() {
        WebSettings webSetting = nativeWeb.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }
}
