package com.mgosu.imagepreview.data.roomdatabase.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.mgosu.imagepreview.data.roomdatabase.model.FavoriteModel;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM FavoriteModel")
    List<FavoriteModel> getAllItem();

    @Query("SELECT * FROM FavoriteModel WHERE Type = :type")
    List<FavoriteModel> getItemFromType(String type);

    @Query("DELETE FROM FavoriteModel WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM FavoriteModel WHERE Url = :url")
    void deleteByUrl(String url);

    @Query("SELECT * FROM FavoriteModel WHERE id = :id")
    FavoriteModel getItemFromId(int id);

    @Query("SELECT * FROM FavoriteModel WHERE Url = :url")
    FavoriteModel getItemFromUrl(String url);

    @Delete
    void deleteByModel(FavoriteModel... models);

    @Insert
    void insertItemModel(FavoriteModel... models);

}
