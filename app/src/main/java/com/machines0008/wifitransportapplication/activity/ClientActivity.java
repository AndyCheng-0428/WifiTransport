package com.machines0008.wifitransportapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.machines0008.wifitransportapplication.GlobalConstant;
import com.machines0008.wifitransportapplication.FileContract;
import com.machines0008.wifitransportapplication.R;
import com.machines0008.wifitransportapplication.Singleton;
import com.machines0008.wifitransportapplication.client.ClientWorkThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientActivity extends BaseActivity {
    private static final String TAG = ClientActivity.class.getSimpleName();
    private Button btnSavePath;
    private Button btnStart;
    private TextView tvSavePath;
    private static final int FILE_CHOOSER_REQUEST_CODE = 2001;
    private static final int FILE_WRITTEN_REQUEST_CODE = 2002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    /***
     * 是否需要要求檔案儲存"特殊權限"
     * 在Android 11+手機以上須該權限。
     * @return 若Android版本在11以下，則無需該權限，若已有該權限，亦無需要求該權限。
     */
    private boolean needRequestRStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return false;
        }
        if (!Environment.isExternalStorageManager()) {
            return false;
        }
        return true;
    }

    /**
     * 請求Android11+檔案儲存特殊權限
     */
    private void requestRStoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, FILE_WRITTEN_REQUEST_CODE);
    }

    private void initListener() {
        btnSavePath.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);
        });
        btnStart.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tvSavePath.getText())) {
                return;
            }
            if (needRequestRStoragePermission()) {
                requestRStoragePermission();
                return;
            }
            new ClientWorkThread(tvSavePath.getText().toString()).start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_CHOOSER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (null != data) {
                        Log.i(TAG, GlobalConstant.FILE_PATH);
                        tvSavePath.setText(GlobalConstant.FILE_PATH);
                    }
                }
                break;
            case FILE_WRITTEN_REQUEST_CODE:
                break;
        }
    }

    private void initView() {
        btnSavePath = findViewById(R.id.btnSavePath);
        btnStart = findViewById(R.id.btnStart);
        tvSavePath = findViewById(R.id.tvSavePath);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_client;
    }
}