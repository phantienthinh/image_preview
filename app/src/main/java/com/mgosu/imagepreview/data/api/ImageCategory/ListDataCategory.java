
package com.mgosu.imagepreview.data.api.ImageCategory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListDataCategory {

    @SerializedName("sub_url")
    @Expose
    private String subUrl;
    @SerializedName("list")
    @Expose
    private List<ListCategoryItem> list = null;

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }

    public List<ListCategoryItem> getList() {
        return list;
    }

    public void setList(List<ListCategoryItem> list) {
        this.list = list;
    }

}
