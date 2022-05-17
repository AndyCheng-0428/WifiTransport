package com.machines0008.wifitransportapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.machines0008.wifitransportapplication.R;
import com.machines0008.wifitransportapplication.server.ServerManager;
import com.machines0008.wifitransportapplication.utils.FileUtils;

import java.io.File;

public class ServerActivity extends BaseActivity {
    private Button btnFile;
    private Button btnTransport;
    private TextView tvFilePath;
    private TextView tvFileName;
    private static final int FILE_CHOOSER_REQUEST_CODE = 1001;
    private ServerManager serverManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initServer();
    }

    private void initServer() {
        serverManager = new ServerManager();
    }

    private void initListener() {
        btnFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);
        });
        btnTransport.setOnClickListener(v -> {
            try {
                if (TextUtils.isEmpty(tvFilePath.getText())) {
                    Toast.makeText(this, "尚未選擇檔案", Toast.LENGTH_SHORT).show();
                    return;
                }
                serverManager.setTransportFile(new File(tvFilePath.getText().toString()));
                serverManager.service();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initView() {
        btnFile = findViewById(R.id.btnFile);
        btnTransport = findViewById(R.id.btnTransport);
        tvFileName = findViewById(R.id.tvFileName);
        tvFilePath = findViewById(R.id.tvFilePath);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FILE_CHOOSER_REQUEST_CODE:
                if (resultCode != RESULT_OK || null == data) {
                    tvFilePath.setText("");
                    tvFileName.setText("");
                    return;
                }
                Uri uri = data.getData();
                String url = FileUtils.getPath(this, uri);
                if (null == url) {
                    tvFilePath.setText("");
                    tvFileName.setText("");
                    return;
                }
                String[] urlArr = url.split("/");
                tvFilePath.setText(url);
                tvFileName.setText(urlArr[urlArr.length - 1]);
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_server;
    }
}