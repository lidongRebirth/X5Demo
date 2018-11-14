package com.myfittinglife.app.x5demo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebChromeClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者
 * 修改
 * 时间    11.13
 * 描述    使用X5内核
 */
public class CompareActivity extends AppCompatActivity {

    @BindView(R.id.native_web)
    WebView nativeWeb;
    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.btn_load)
    Button btnLoad;
    @BindView(R.id.x5_web)
    com.tencent.smtt.sdk.WebView x5Web;

    private static final String TAG = "MainActivity_ceshi";
    @BindView(R.id.ib_back)         //返回按钮
    ImageButton ibBack;
    @BindView(R.id.tv_middle)       //标题
    TextView tvMiddle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        x5Web.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(com.tencent.smtt.sdk.WebView webView, String title) {
                super.onReceivedTitle(webView, title);
                if(title!=null){
                    tvMiddle.setText(title);
                }
            }
        });


    }

    @OnClick({R.id.btn_load,R.id.ib_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_load:             //加载网页
                //原生webview
                nativeWeb.loadUrl(etUrl.getText().toString());
                WebSettings webSettings = nativeWeb.getSettings();
                webSettings.setJavaScriptEnabled(true);
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

                //X5webview
                x5Web.loadUrl(etUrl.getText().toString());
                WebSettings webSetting2 = nativeWeb.getSettings();
                webSetting2.setJavaScriptEnabled(true);
                x5Web.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {
                        return super.shouldOverrideUrlLoading(webView, s);
                    }

                });
                if (x5Web.getX5WebViewExtension() != null) {
                    Log.i(TAG, "已加载了x5内核");
                } else {
                    Log.i(TAG, "未加载X5内核");
                }


                Log.i(TAG, "onCreate: " + x5Web.getView().getWidth());
                break;
            case R.id.ib_back:
                finish();
                break;
        }
    }
}
