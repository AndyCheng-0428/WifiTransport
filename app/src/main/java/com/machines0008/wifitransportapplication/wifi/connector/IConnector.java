package com.machines0008.wifitransportapplication.wifi.connector;

import android.content.Context;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/13
 * Usage:
 **/
public interface IConnector {

    /**
     * 連接WIFI
     * @param context
     * @param ssid
     * @param password
     */
    void connect(Context context, String ssid, String password);
}
