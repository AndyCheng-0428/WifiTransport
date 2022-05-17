package com.machines0008.wifitransportapplication;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/13
 * Usage:
 **/
public class WifiApplication extends android.app.Application {
    private static WifiApplication wifiApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        wifiApplication = this;
    }

    public static WifiApplication getInstance() {
        return wifiApplication;
    }
}
