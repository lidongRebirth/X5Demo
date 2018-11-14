package com.myfittinglife.app.x5demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
*  作者    LD
*  修改
*  时间    2018.11.13 17:17
*  描述    继承自腾讯WebView的控件，对其进行了更改
*/
public class X5WebView extends WebView {
	TextView title;
	private static final String TAG = "X5WebView_ceshi";

	private WebViewClient client = new WebViewClient() {
		/**
		 * 防止加载网页时调起系统浏览器
		 */
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
            Log.i(TAG, "shouldOverrideUrlLoading: 触发，为重定向地址，所以加载两次");
			Log.i(TAG, "shouldOverrideUrlLoading: 重定向的地址为："+url);
			return true;
		}
	};

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		this.setWebViewClient(client);
		// this.setWebChromeClient(chromeClient);
		// WebStorage webStorage = WebStorage.getInstance();
		initWebViewSettings();
		this.getView().setClickable(true);
	}

	private void initWebViewSettings() {
		WebSettings webSetting = this.getSettings();
		webSetting.setJavaScriptEnabled(true);														//支持JS
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);									//通过JS打开新的窗口
		webSetting.setAllowFileAccess(true);														//设置可以访问文件
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);							    //适应内容大小
		webSetting.setSupportZoom(true);															//支持缩放
		webSetting.setBuiltInZoomControls(true);													//设置内置的缩放控件
		webSetting.setUseWideViewPort(true);														//将图片调整到适合webview的大小
		webSetting.setSupportMultipleWindows(true);													//设置webview是否支持多窗口
		// webSetting.setLoadWithOverviewMode(true);												//缩放至屏幕大小
		webSetting.setAppCacheEnabled(true);														//设置是否应该启用应用程序缓存API。默认值为false。注意，为了启用应用程序缓存API，还必须向setAppCachePath(String)提供有效的数据库路径。
		// webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);														//设置是否启用DOM存储API
		webSetting.setGeolocationEnabled(true);														//设置是否启用地理定位
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);												//设置应用程序缓存内容的最大大小
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);								//告诉WebView按需启用、禁用或拥有插件
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染优先级
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);											//设置缓存方式

		// this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计
        //5.0以后解决WebView加载的链接为Https开头，但链接里的内容，如图片为http链接，图片会加载不出来
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //两者都可以
            webSetting.setMixedContentMode(webSetting.getMixedContentMode());
            //mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
	}

	/**
	 * 绘制一些系统内核、手机的信息
	 * @param canvas
	 * @param child
	 * @param drawingTime
	 * @return
	 */
	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		boolean ret = super.drawChild(canvas, child, drawingTime);
		canvas.save();
		Paint paint = new Paint();
		paint.setColor(0x7fff0000);
		paint.setTextSize(24.f);
		paint.setAntiAlias(true);
		if (getX5WebViewExtension() != null) {
			canvas.drawText(this.getContext().getPackageName() + "-pid:"
					+ android.os.Process.myPid(), 10, 50, paint);
			canvas.drawText(
					"X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 10,
					100, paint);
		} else {
			canvas.drawText(this.getContext().getPackageName() + "-pid:"
					+ android.os.Process.myPid(), 10, 50, paint);
			canvas.drawText("Sys Core", 10, 100, paint);
		}
		canvas.drawText(Build.MANUFACTURER, 10, 150, paint);
		canvas.drawText(Build.MODEL, 10, 200, paint);
		canvas.restore();
		return ret;
	}

	public X5WebView(Context arg0) {
		super(arg0);
		setBackgroundColor(85621);
	}


}
