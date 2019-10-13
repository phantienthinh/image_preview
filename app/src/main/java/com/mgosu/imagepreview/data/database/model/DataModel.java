package com.mgosu.imagepreview.data.database.model;

public class DataModel {
    private int categories_id;
    private String data_value;
    private int data_id;
    private int bookmark;
    private int read;
    private int data_lang;

    public DataModel() {
    }

    public DataModel(int categories_id, String data_value, int data_id, int bookmark, int read, int data_lang) {
        this.categories_id = categories_id;
        this.data_value = data_value;
        this.data_id = data_id;
        this.bookmark = bookmark;
        this.read = read;
        this.data_lang = data_lang;
    }

    public int getCategories_id() {
        return categories_id;
    }

    public void setCategories_id(int categories_id) {
        this.categories_id = categories_id;
    }

    public String getData_value() {
        return data_value;
    }

    public void setData_value(String data_value) {
        this.data_value = data_value;
    }

    public int getData_id() {
        return data_id;
    }

    public void setData_id(int data_id) {
        this.data_id = data_id;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getData_lang() {
        return data_lang;
    }

    public void setData_lang(int data_lang) {
        this.data_lang = data_lang;
    }
}
