package com.machines0008.wifitransportapplication.activity;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/17
 * Usage:
 **/
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    public abstract @LayoutRes int getLayoutId();
}
