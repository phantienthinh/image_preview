package com.mgosu.imagepreview.data.database.model;

public class CategoryModel {
    private int pk_category;
    private int category_id;
    private String category_name;
    private int category_count;
    private int category_lang;

    public CategoryModel(int pk_category, int category_id, String category_name, int category_count, int category_lang) {
        this.pk_category = pk_category;
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_count = category_count;
        this.category_lang = category_lang;
    }

    public CategoryModel() {
    }

    public int getPk_category() {
        return pk_category;
    }

    public void setPk_category(int pk_category) {
        this.pk_category = pk_category;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_count() {
        return category_count;
    }

    public void setCategory_count(int category_count) {
        this.category_count = category_count;
    }

    public int getCategory_lang() {
        return category_lang;
    }

    public void setCategory_lang(int category_lang) {
        this.category_lang = category_lang;
    }
}
