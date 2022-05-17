package com.machines0008.wifitransportapplication.server;

import java.io.File;
import java.io.IOException;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/17
 * Usage:
 **/
public class ServerManager {
    private File transportFile;
    private ServerWorkThread workThread;

    public void service() throws IOException {
        if (!canService()) {
            return;
        }
        shutdown();
        workThread = new ServerWorkThread(transportFile);
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
}
