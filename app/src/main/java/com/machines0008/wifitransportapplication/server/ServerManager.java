package com.machines0008.wifitransportapplication.server;

import android.util.Base64;

import com.machines0008.wifitransportapplication.FileContract;
import com.machines0008.wifitransportapplication.Singleton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/17
 * Usage:
 **/
public class ServerManager {
    private File transportFile;
    private WorkThread workThread;
    private static final int TIME_OUT = 30;
    private static final int THREAD_COUNT = 3;
    private static final int PORT = 8080;

    public void service() throws IOException {
        if (!canService()) {
            return;
        }
        shutdown();
        workThread = new WorkThread(transportFile);
        workThread.start();
    }

    private boolean canService() {
        if (transportFile == null) {
            return false;
        }
        return true;
    }

    public void setTransportFile(File transportFile) {
        this.transportFile = transportFile;
    }

    public void shutdown() {
        if (workThread == null) {
            return;
        }
        workThread.shutdown();
    }

    private static class WorkThread extends Thread {
        private ExecutorService executorService;
        private ServerSocket serverSocket;
        private File transportFile;
        private volatile boolean isInterrupted = false;

        public WorkThread(File transportFile) throws IOException {
            this.executorService = Executors.newFixedThreadPool(THREAD_COUNT);
            this.serverSocket = new ServerSocket(PORT);
            this.transportFile = transportFile;
        }

        @Override
        public void run() {
            while (!isInterrupted) {
                try {
                    Socket socket = serverSocket.accept();
                    socket.setSoTimeout(TIME_OUT * 1000);
                    executorService.execute(new Handler(socket, transportFile));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            executorService.shutdown();
            try {
                if (!serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void shutdown() {
            this.isInterrupted = true;
        }
    }

    public static class Handler implements Runnable {
        private final Socket socket;
        private final File transportFile;

        public Handler(Socket socket, File transportFile) {
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
}
