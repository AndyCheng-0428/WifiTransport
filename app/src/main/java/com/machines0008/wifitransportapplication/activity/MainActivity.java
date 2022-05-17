package com.machines0008.wifitransportapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.machines0008.wifitransportapplication.R;
import com.machines0008.wifitransportapplication.wifi.WifiManager;

public class MainActivity extends BaseActivity {
    private WifiManager wifiManager;
    private Button btnServer;
    private Button btnClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wifiManager = new WifiManager(this);
        wifiManager.startScan();
        btnServer = findViewById(R.id.btnServer);
        btnClient = findViewById(R.id.btnClient);
        btnServer.setOnClickListener(v -> {
            Intent intent = new Intent(this, ServerActivity.class);
            startActivity(intent);
        });
        btnClient.setOnClickListener(v ->  {
            startActivity(new Intent(this, ClientActivity.class));
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        wifiManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wifiManager.release();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}