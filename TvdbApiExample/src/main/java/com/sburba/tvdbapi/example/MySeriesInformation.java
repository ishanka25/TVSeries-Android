package com.sburba.tvdbapi.example;

/**
 * Created by DILSHAN FERNANDO on 4/24/2016.
 */
public class MySeriesInformation {

    int sId;
    String title;
    String imgPath;
    private final String PATH = "/data/data/com.sburba.tvdbapi.example/";
    public MySeriesInformation() {
        this.sId = -1;
        this.title = "";
        this.imgPath = "";
    }

    public int getsId() {
        return sId;
    }

    public String getTitle() {
        return title;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setsId(int sId) {
        this.sId = sId;
        this.setImgPath(sId);

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgPath(int sid) {
        this.imgPath = PATH+Integer.toString(sid)+"/Banner.jpg";
    }
}
