package com.iotaddon.goout;

/**
 * Created by Ryu on 2017-05-28.
 */

public class ItemContents {
    private int contentsType;
    private String title;
    private String contents;

    public ItemContents(int contentsType, String title, String contents) {
        this.contentsType = contentsType;
        this.title = title;
        this.contents = contents;
    }

    public int getContentsType() {
        return contentsType;
    }

    public void setContentsType(int contentsType) {
        this.contentsType = contentsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
