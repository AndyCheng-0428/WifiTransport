package com.machines0008.wifitransportapplication.wifi.chooser;

import static android.content.Context.WIFI_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/13
 * Usage:
 **/

public class NChooser extends IChooser {
    private Context context;

    public NChooser(Context context) {
        this.context = context;
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public void startScan() {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        wifiManager.startScan();
        List<ScanResult> result = wifiManager.getScanResults();
        List<String> itemName = new ArrayList<>();
        List<ScanResult> itemScanResult = new ArrayList<>();
        for (int i = 0, size = result.size(); i < size; i++) {
            String ssid = result.get(i).SSID;
            if (TextUtils.isEmpty(ssid)) {
                continue;
            }
            itemName.add(ssid);
            itemScanResult.add(result.get(i));
        }
        new AlertDialog.Builder(context)
                .setTitle("選擇WIFI")
                .setItems(itemName.toArray(new String[itemName.size()]), (dialog1, which) -> {
                    if (null == listener) {
                        return;
                    }
                    listener.onWifiEnable(itemScanResult.get(which));
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void release() {
        context = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }
}
