package com.myfittinglife.app.x5demo;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;

/**
 * 作者     LD
 * 时间    2018/11/13 14:32
 * 描述     初始化操作
 */
public class AppApplication extends Application {
    private static final String TAG = "AppApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        initX5Sdk();
    }
    public void initX5Sdk(){    //预加载X5内核，提高网页加载速度，不会导致白屏现象发生
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }
            @Override
            public void onViewInitFinished(boolean b) {
                //x5内核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核
                Log.d(TAG, "onCoreInitFinished: "+b);
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),cb);
    }
}
