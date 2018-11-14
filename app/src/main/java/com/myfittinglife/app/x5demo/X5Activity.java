package com.myfittinglife.app.x5demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.myfittinglife.app.x5demo.utils.X5WebView;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * 作者    LD
 * 修改
 * 时间    11.13 16:56
 * 描述    X5浏览器使用
 */


/**
 * url
 * 爱奇艺：http://iqiyi.com
 * QQ音乐：https://y.qq.com/n/yqq/playsquare/2961613734.html#stat=y_new.index.playlist.pic
 */
public class X5Activity extends AppCompatActivity {

    @BindView(R.id.x5_web)
    X5WebView x5Web;
    @BindView(R.id.et_url)          //网址
            EditText etUrl;
    @BindView(R.id.btn_load)        //加载
            Button btnLoad;
    @BindView(R.id.ib_back)         //返回
    ImageButton ibBack;
    @BindView(R.id.tv_middle)       //标题
    TextView tvMiddle;

    private String url_iqiyi = "http://iqiyi.com";
    private String url_QQYinYue = "https://y.qq.com/n/yqq/playsquare/2961613734.html#stat=y_new.index.playlist.pic";
    private String getUrl_QQYinYue2 = "https://y.qq.com/n/m/detail/taoge/index.html?ADTAG=newyqq.taoge&id=2961613734";      //重定向的地址

    private String payQQUrl="https://y.qq.com/portal/search.html#page=1&searchid=1&remoteplace=txt.yqq.top&t=song&w=%E6%9E%97%E4%BF%8A%E6%9D%B0";   //付费才能下载
//    private String payWangYiUrl="https://music.163.com/#/search/m/?id=3684&s=%E4%BB%98%E8%B4%B9%E9%9F%B3%E4%B9%90&type=1";
    private String payWangYiUrl="https://music.163.com/#/search/m/?id=3684&s=%E4%BB%98%E8%B4%B9%E9%9F%B3%E4%B9%90&type=1";//付费才能听
    private String getPayWangYiUrl2 = "https://music.163.com/#/artist?id=6731";                                           //付费才能下载
    private static final String TAG = "X5Activity_ceshi";
    private String downloadUrl="http://221.204.28.28/amobile.music.tc.qq.com/C400002K4xqW4A7m7q.m4a?guid=1962737099&vkey=6002B7C334A29777886CFDEB5FA968DA6684D12235F68F2623F3603F8BD6113101C70A2D2F2AFE917EA649F2A0330793673D1991DAF25B53&uin=0&fromtag=38";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5);
        ButterKnife.bind(this);
        x5Web.loadUrl(getPayWangYiUrl2);
        x5Web.getSettings().setUserAgent("电脑");//防止网易云检测到是手机而让下载
        x5Web.setVerticalScrollBarEnabled(false);

        //下面方法去掉滑动页面时右边出现的滑动小方块
        IX5WebViewExtension ix5 = x5Web.getX5WebViewExtension();
        if (null != ix5) {
            ix5.setScrollBarFadingEnabled(false);
        }



        x5Web.setWebViewClient(new WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            public boolean shouldOverrideUrlLoading(WebView view, String url) {     //8.0以后返回false才会重定向，并且无需调用loadUrl
                if(Build.VERSION.SDK_INT<26){
                    view.loadUrl(url);
                    Log.i(TAG, "shouldOverrideUrlLoading: 触发，为重定向地址，所以加载两次");
                    Log.i(TAG, "shouldOverrideUrlLoading: 重定向的地址为："+url);
                    return true;
                }
                return false;

            }
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                Log.i(TAG, "onPageStart: " + "页面开始加载");         //有时开始加载会被触发两次，因为加载的地址需要重定向，会调用shouldOverrideUrlLoading()，所以加载两次
            }

            @Override
            public void onPageFinished(WebView webView, String s) {        //若X5WebView中已经设置WebViewClient，此处再设置会导致onPageFinished()调用三次，什么原因？？？？
                super.onPageFinished(webView, s);
                Log.i(TAG, "onPageFinished: " + "页面加载完成");
            }

            @Override
            public void onLoadResource(WebView webView, String songUrl) {
                super.onLoadResource(webView, songUrl);
//                Log.i(TAG, "onLoadResource: "+s);
                //判断地址
                if(songUrl.contains("m4a?")||songUrl.contains(".mp3")){         //网易为.MP3腾讯为m4a
                    Log.i(TAG, "onLoadResource: 下载地址为："+songUrl);
                }
            }



        });
        /**
         * 注意：1、注意获取到为空的现象
         *      2、在一些机型上面，Webview.goBack()后，这个方法不一定会调用，所以标题还是之前页面的标题。       怎么用？？？
         *         那么你就需要用一个ArrayList来保持加载过的url,一个HashMap保存url及对应的title.然后就是用WebView.canGoBack()来做判断处理了。
         */
        x5Web.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView webView, String title) {        //设置标题栏为网页的标题
                super.onReceivedTitle(webView, title);
                if(title!=null){
                    tvMiddle.setText(title);
                }
            }

            @Override
            public void onProgressChanged(WebView webView, int progress) {
                if(progress==100){
                    //加载完毕
                }else {
                    //更新进度
                }



                super.onProgressChanged(webView, progress);
            }
        });
    }



//*----------------------------------------------------------------------
    @OnClick({R.id.btn_load,R.id.ib_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_load:         //加载
                if (!TextUtils.isEmpty(etUrl.getText())) {
                    String url = etUrl.getText().toString();
                    x5Web.loadUrl(url);
                } else {
                    Toast.makeText(getApplicationContext(), "请输入网址", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ib_back:          //返回按钮
                finish();
                break;

        }
    }
    //返回设置
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KEYCODE_BACK&&x5Web.canGoBack()){
            x5Web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        x5Web.onResume();            //方法二：解决关闭网页音乐播放问题，不管用
        x5Web.resumeTimers();
        super.onResume();
    }

    @Override
    protected void onPause() {
        x5Web.onPause();            //暂停部分可安全处理的操作，如动画，定位，视频播放等
        x5Web.pauseTimers();        //暂停所有WebView的页面布局、解析以及JavaScript的定时器操作
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //----解决Webview打开一个页面，播放一段音乐，退出Activity时音乐还在后台播放
        //方法一：销毁WebView(别问我为什么要移除，等你Error: WebView.destroy() called while still attached!之后你就知道了。)
        if (x5Web != null) {
            x5Web.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            x5Web.clearHistory();
            ((ViewGroup) x5Web.getParent()).removeView(x5Web);
            x5Web.destroy();
            x5Web = null;
        }
        super.onDestroy();
    }



}
