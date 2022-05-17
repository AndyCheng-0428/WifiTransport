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

import androidx.annotation.Nullable;

import com.machines0008.wifitransportapplication.Default;
import com.machines0008.wifitransportapplication.FileContract;
import com.machines0008.wifitransportapplication.R;
import com.machines0008.wifitransportapplication.Singleton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientActivity extends BaseActivity {
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, FILE_WRITTEN_REQUEST_CODE);
            }
        }
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
            new Thread() {
                @Override
                public void run() {
                    try (Socket socket = new Socket()) {
                        socket.setSoTimeout(30 * 1000);
                        InetSocketAddress inetSocketAddress = new InetSocketAddress("192.168.0.159", 8080);
                        socket.connect(inetSocketAddress);
                        try (InputStream is = socket.getInputStream();
                             InputStreamReader isr = new InputStreamReader(is);
                             BufferedReader br = new BufferedReader(isr);
                        ) {
                            String str = null;
                            File file = new File(tvSavePath.getText().toString());
                            if (!file.exists()) {
                                boolean isMkdirs = file.mkdirs();
                                Log.i("123456", "isMkDirs = " + isMkdirs);
                            }
                            File copyFile = null;
                            FileOutputStream fos = null;
                            while ((str = br.readLine()) != null) {
                                FileContract contract = Singleton.gson.fromJson(str, FileContract.class);
                                if (null == copyFile) {
                                    copyFile = new File(file.getAbsolutePath() + "/" + contract.getName());
                                    fos = new FileOutputStream(copyFile);
                                }
                                fos.write(Base64.decode(contract.getContent(), Base64.NO_WRAP));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_CHOOSER_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (null != data) {
                        Log.i("ClientActivity", Default.FILE_PATH);
                        tvSavePath.setText(Default.FILE_PATH);
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