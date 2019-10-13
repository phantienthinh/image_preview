package com.mgosu.imagepreview.data.roomdatabase;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mgosu.imagepreview.data.roomdatabase.dao.FavoriteDao;
import com.mgosu.imagepreview.data.roomdatabase.model.FavoriteModel;


@Database(entities = {FavoriteModel.class}, version = 1, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {
    private static final String DB_NAME = "Favorite.db";

    public abstract FavoriteDao favoriteDao();


    private static RoomDb instance;

    public static RoomDb getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, RoomDb.class, DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

}
