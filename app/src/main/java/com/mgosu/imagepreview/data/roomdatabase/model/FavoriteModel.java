package com.mgosu.imagepreview.data.roomdatabase.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Comparator;

@Entity
public class FavoriteModel implements Serializable  {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "Url")
    private String Url;
    @ColumnInfo(name = "Type")
    private String Type;
    @ColumnInfo(name = "Title")
    private String Title;


    public FavoriteModel() {
    }

    public FavoriteModel(String url, String type,String Title) {
        this.Url = url;
        this.Type = type;
        this.Title = Title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }


    public static final Comparator<FavoriteModel> ID = new Comparator<FavoriteModel>() {
        @Override
        public int compare(FavoriteModel o1, FavoriteModel o2) {
            if (o2.id > o1.id) {
                return 1;
            } else if (o2.id < o1.id) {
                return -1;
            } else {
                return 0;
            }
        }
    };
}

