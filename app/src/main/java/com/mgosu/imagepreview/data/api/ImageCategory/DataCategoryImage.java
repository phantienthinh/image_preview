
package com.mgosu.imagepreview.data.api.ImageCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataCategoryImage {

    @SerializedName("data")
    @Expose
    private ListDataCategory data;

    public ListDataCategory getData() {
        return data;
    }

    public void setData(ListDataCategory data) {
        this.data = data;
    }

}
