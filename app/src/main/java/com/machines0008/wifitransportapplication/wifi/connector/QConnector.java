package com.machines0008.wifitransportapplication.wifi.connector;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/13
 * Usage:
 **/
public class QConnector implements IConnector {
    private static final String TAG = QConnector.class.getSimpleName();

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void connect(Context context, String ssid, String password) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                .setSsid(ssid)
                .setWpa2Passphrase(password)
                .build();
        final NetworkRequest request = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_FOREGROUND)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_CONGESTED)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_ROAMING)
                .setNetworkSpecifier(specifier)
                .build();
        connectivityManager.requestNetwork(request, new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                connectivityManager.bindProcessToNetwork(network);
                Log.i(TAG, "連接成功");
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Log.i(TAG, "連接失敗");
                Toast.makeText(context, "連接失敗", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
