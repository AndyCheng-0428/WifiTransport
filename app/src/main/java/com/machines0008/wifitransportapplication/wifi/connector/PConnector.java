package com.machines0008.wifitransportapplication.wifi.connector;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/13
 * Usage:
 **/
public class PConnector implements IConnector {
    private static final String TAG = QConnector.class.getSimpleName();

    @Override
    public void connect(Context context, String ssid, String password) {
        String transferSsid = String.format("\"%s\"", ssid);
        String transferPassword = String.format("\"%s\"", password);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedProtocols.clear();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.SSID = transferSsid;
        config.preSharedKey = transferPassword;
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int wifiId = wifiManager.addNetwork(config);
        boolean result = wifiManager.enableNetwork(wifiId, true);
        Log.i(TAG, "連線" + (result ? "成功" : "失敗"));
        Toast.makeText(context, "連線" + (result ? "成功" : "失敗"), Toast.LENGTH_SHORT).show();
    }
}
