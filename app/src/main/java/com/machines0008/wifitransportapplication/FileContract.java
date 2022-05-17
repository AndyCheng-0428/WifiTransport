package com.machines0008.wifitransportapplication;

/**
 * Project Name: WifiTransportApplication
 * Created By: user
 * Created On: 2022/5/17
 * Usage:
 **/
public class FileContract {
    private String name; //檔名
    private long size; //單位byte
    private int seq; //檔案序號(檔案過大時會拆成每2048byte為一封包並轉為BASE64字串進行傳輸)
    private String content; //檔案內容

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
