package com.myfittinglife.app.x5demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者    LD
 * 修改
 * 时间    11.13 16:49
 * 描述    启动页
 */
public class StartActivity extends AppCompatActivity {

    @BindView(R.id.btn_compare)
    Button btnCompare;
    @BindView(R.id.btn_x5)
    Button btnX5;
    @BindView(R.id.btn_native)
    Button btnNative;
    @BindView(R.id.btn_openalbum)       //打开相册
    Button btnOpenalbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_compare, R.id.btn_x5, R.id.btn_native,R.id.btn_openalbum})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_compare:
                startActivity(new Intent(this, CompareActivity.class));
                break;
            case R.id.btn_x5:
                startActivity(new Intent(this, X5Activity.class));
                break;
            case R.id.btn_native:
                startActivity(new Intent(this, NativeWebActivity.class));
                break;
            case R.id.btn_openalbum:        //打开相册
                startActivity(new Intent(this,OpenAlbumActivity.class));
                break;
        }
    }
}
