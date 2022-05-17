package com.machines0008.wifitransportapplication.server;

import android.util.Base64;

import com.machines0008.wifitransportapplication.FileContract;
import com.machines0008.wifitransportapplication.Singleton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/17
 * Usage: 伺服器端收到請求後，所需操作之子任務
 **/
public class ServerSubWorkRunnable implements Runnable {
    private final Socket socket;
    private final File transportFile;

    public ServerSubWorkRunnable(Socket socket, File transportFile) {
        this.socket = socket;
        this.transportFile = transportFile;
    }

    @Override
    public void run() {
        try (FileInputStream fis = new FileInputStream(transportFile);
             OutputStream os = socket.getOutputStream();
             OutputStreamWriter osw = new OutputStreamWriter(os);
             BufferedWriter bw = new BufferedWriter(osw)) {
            byte[] bytes = new byte[2048];
            int seq = 1;
            while (fis.read(bytes) != -1) {
                FileContract fileContract = new FileContract();
                fileContract.setName(transportFile.getName());
                fileContract.setSeq(seq++);
                fileContract.setSize(transportFile.length());
                fileContract.setContent(Base64.encodeToString(bytes, Base64.NO_WRAP));
                bw.write(Singleton.gson.toJson(fileContract));
                bw.write("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
