package com.machines0008.wifitransportapplication.wifi.chooser;

import android.app.Activity;
import android.companion.AssociationRequest;
import android.companion.CompanionDeviceManager;
import android.companion.WifiDeviceFilter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/13
 * Usage:
 **/
@RequiresApi(api = Build.VERSION_CODES.O)
public class OChooser extends IChooser {
    private static final String TAG = OChooser.class.getSimpleName();
    private Context context;
    private final int REQUEST_CODE;

    public OChooser(Context context, final int REQUEST_CODE) {
        this.context = context;
        this.REQUEST_CODE = REQUEST_CODE;
    }

    public void startScan() {
        if (!(context instanceof Activity)) {
            return;
        }
        CompanionDeviceManager deviceManager = (CompanionDeviceManager) context.getSystemService(Context.COMPANION_DEVICE_SERVICE);
        WifiDeviceFilter filter = new WifiDeviceFilter.Builder()
                .build();
        AssociationRequest request = new AssociationRequest.Builder()
                .addDeviceFilter(filter)
                .build();
        deviceManager.associate(request, new CompanionDeviceManager.Callback() {
            @Override
            public void onDeviceFound(IntentSender chooserLauncher) {
                try {
                    ((Activity) context).startIntentSenderForResult(chooserLauncher, REQUEST_CODE, null, 0, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(CharSequence error) {
                Log.i(TAG, error.toString());
            }
        }, null);
    }

    @Override
    public void release() {
        context = null;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (null == data) {
            return;
        }
        if (requestCode != REQUEST_CODE) {
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (null == listener) {
            return;
        }
        listener.onWifiEnable(data.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE));
    }
}
