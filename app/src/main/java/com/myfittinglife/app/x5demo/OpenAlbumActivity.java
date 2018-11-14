package com.myfittinglife.app.x5demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.myfittinglife.app.x5demo.utils.X5WebView;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者    LD
 * 修改
 * 时间    2018.11.14 15:23
 * 描述    webview手机相册
 */
public class OpenAlbumActivity extends AppCompatActivity {

    @BindView(R.id.x5_web)
    X5WebView x5Web;
    @BindView(R.id.iv_pic)
    ImageView ivPic;

    public ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;
    private final static int FILE_CHOOSER_RESULT_CODE = 1;// 表单的结果回调
    public final static int FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5 = 2;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String s = msg.getData().getString("data");
                    Glide.with(getApplicationContext()).load(s).into(ivPic);
                    break;
            }
            super.handleMessage(msg);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_album);
        ButterKnife.bind(this);
        x5Web.loadUrl("file:///android_asset/demo.html");

        x5Web.setWebChromeClient(new WebChromeClient() {

            //  android 3.0以下：用的这个方法
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }
            // android 3.0以上，android4.0以下：用的这个方法
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }
            //android 4.0 - android 4.3  安卓4.4.4也用的这个方法
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType,
                                        String capture) {
                mUploadMessage = valueCallback;
                openImageChooserActivity();
            }
            //android4.4 无方法。。。
            // Android 5.0及以上用的这个方法
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                mUploadMessageForAndroid5 = filePathCallback;
                openImageChooserActivity5();
                return true;
            }
        });


    }




    public void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    public void openImageChooserActivity5() {
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        startActivityForResult(chooserIntent, FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Uri result = (data == null || resultCode != Activity.RESULT_OK) ? null : data.getData();
        switch (requestCode) {
            case FILE_CHOOSER_RESULT_CODE:  //android 5.0 以下 选择图片回调
                if (null == mUploadMessage)
                    return;
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
                Log.i("ceshidizhi", "onActivityResult:" + result);

                break;

            case FILE_CHOOSER_RESULT_CODE_FOR_ANDROID_5:  //android 5.0(含) 以上 选择图片回调
                if (null == mUploadMessageForAndroid5)
                    return;
                if (result != null) {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
                } else {
                    mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
                }
                mUploadMessageForAndroid5 = null;



                Log.i("ceshidizhi", "onActivityResult:" + result);
                if(Looper.getMainLooper()!=Looper.myLooper()){
                    Log.i("ceshidizhi", "主线程 ");
                }else{
                    Log.i("ceshidizhi", "非主线程");
                    Message message = new Message();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("data",result.toString());
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                break;
        }


    }
}
