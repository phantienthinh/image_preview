
package com.mgosu.imagepreview.data.api.database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataAll {

    @SerializedName("data")
    @Expose
    private DataBaseModel data;

    public DataBaseModel getData() {
        return data;
    }

    public void setData(DataBaseModel data) {
        this.data = data;
    }

}
