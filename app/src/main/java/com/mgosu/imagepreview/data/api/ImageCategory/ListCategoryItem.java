
package com.mgosu.imagepreview.data.api.ImageCategory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListCategoryItem implements Parcelable {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("folder")
    @Expose
    private String folder;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("items")
    @Expose
    private List<String> items = null;

    protected ListCategoryItem(Parcel in) {
        title = in.readString();
        folder = in.readString();
        thumb = in.readString();
        items = in.createStringArrayList();
    }

    public static final Creator<ListCategoryItem> CREATOR = new Creator<ListCategoryItem>() {
        @Override
        public ListCategoryItem createFromParcel(Parcel in) {
            return new ListCategoryItem(in);
        }

        @Override
        public ListCategoryItem[] newArray(int size) {
            return new ListCategoryItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(folder);
        dest.writeString(thumb);
        dest.writeStringList(items);
    }
}
