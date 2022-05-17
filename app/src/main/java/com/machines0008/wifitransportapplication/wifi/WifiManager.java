package com.machines0008.wifitransportapplication.wifi;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.machines0008.wifitransportapplication.R;
import com.machines0008.wifitransportapplication.wifi.chooser.IChooser;
import com.machines0008.wifitransportapplication.wifi.chooser.NChooser;
import com.machines0008.wifitransportapplication.wifi.chooser.OChooser;
import com.machines0008.wifitransportapplication.wifi.connector.IConnector;
import com.machines0008.wifitransportapplication.wifi.connector.PConnector;
import com.machines0008.wifitransportapplication.wifi.connector.QConnector;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/13
 * Usage:
 **/
public class WifiManager {
    private final IChooser wifiChooser;
    private final IConnector wifiConnector;
    private static final int REQUEST_CODE = 1201;

    public WifiManager(Context context) {
        wifiConnector = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? new QConnector() : new PConnector();
        wifiChooser = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? new OChooser(context, REQUEST_CODE) : new NChooser(context);
        wifiChooser.addListener(scanResult -> {
            if (wifiConnector == null) {
                return;
            }
            View view = LayoutInflater.from(context).inflate(R.layout.text_input_password, null);
            EditText etPassword = view.findViewById(R.id.input);
            new AlertDialog.Builder(context)
                    .setTitle("WIFI")
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        dialog.dismiss();
                        String password = etPassword.getText().toString();
                        wifiConnector.connect(context, scanResult.SSID, password);
                    }).setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss()).create().show();
        });
    }

    public void startScan() {
        wifiChooser.startScan();
    }

    public void release() {
        wifiChooser.release();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        wifiChooser.onActivityResult(requestCode, resultCode, data);
    }
}
