package com.machines0008.wifitransportapplication.wifi.chooser;

import android.content.Intent;
import android.net.wifi.ScanResult;

import androidx.annotation.Nullable;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/13
 * Usage:
 **/
public abstract class IChooser {
     protected Listener listener;
     public abstract void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);

     public abstract void startScan();

     public abstract void release();

     public void addListener(Listener listener) {
          this.listener = listener;
     }

     public interface Listener {
          void onWifiEnable(ScanResult scanResult);
     }
}
