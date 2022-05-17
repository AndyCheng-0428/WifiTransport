package com.machines0008.wifitransportapplication.server;

import java.io.File;
import java.io.IOException;
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
public class ServerWorkThread extends Thread {
    private static final int TIME_OUT = 30;
    private static final int THREAD_COUNT = 3;
    private static final int PORT = 8080;

    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private File transportFile;
    private volatile boolean isInterrupted = false;

    public ServerWorkThread(File transportFile) throws IOException {
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
                executorService.execute(new ServerSubWorkRunnable(socket, transportFile));
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
