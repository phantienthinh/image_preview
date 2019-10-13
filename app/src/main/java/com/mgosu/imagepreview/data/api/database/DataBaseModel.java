
package com.mgosu.imagepreview.data.api.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataBaseModel {

    @SerializedName("ver")
    @Expose
    private int ver;
    @SerializedName("file")
    @Expose
    private String file;

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
