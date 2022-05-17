package com.machines0008.wifitransportapplication.client;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.machines0008.wifitransportapplication.FileContract;
import com.machines0008.wifitransportapplication.Singleton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/17
 * Usage:
 **/
public class ClientWorkThread extends Thread {
    private final String directoryPath;
    private static final int TIME_OUT = 30;
    private static final int PORT = 8080;

    public ClientWorkThread(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket()) {
            socket.setSoTimeout(TIME_OUT * 1000);
            InetSocketAddress inetSocketAddress = new InetSocketAddress("192.168.0.159", PORT);
            socket.connect(inetSocketAddress);
            FileOutputStream fos = null;
            try (InputStream is = socket.getInputStream();
                 InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader br = new BufferedReader(isr)) {
                File file = new File(directoryPath);
                if (!file.exists()) {
                    boolean mkdirs = file.mkdirs();
                }
                String str;
                File copyFile = null;

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
            } finally {
                if (null != fos) {
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
