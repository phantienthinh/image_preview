package com.mgosu.imagepreview.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mgosu.imagepreview.data.database.model.CategoryModel;
import com.mgosu.imagepreview.data.database.model.DataModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private SQLiteDatabase database;

    private static final String TAG = DBHelper.class.getSimpleName();
    private static String DB_PATH = "/data/data/com.mgosu.football_squad_builder.data/databases/";
    private static String DB_NAME = "foolball.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private String folderPath;
    public static DBHelper instance;


    public static DBHelper getInstance(Context context, String name) {
        if (instance == null) {
            instance = new DBHelper(context, name);
        }
        return instance;
    }

    public DBHelper(Context context, String folderPath) {
        this.myContext = context;
        this.folderPath = folderPath;
    }

    private SQLiteDatabase checkDataBase() {
        File file = new File(folderPath);
        if (file.exists()) {
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
            Log.e("Its open? ", db.isOpen() + " " + db.isReadOnly());
            if (db.isOpen() && !db.isReadOnly()) return db;
        }
        return null;
    }

    public void closeDB() {
        if (database != null) {
            database.close();
            database = null;
        }
    }

    public void createDataBase() {
        database = checkDataBase();

    }

    public List<CategoryModel> getAllCategory() {
        List<CategoryModel> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM categories", null);
        cursor.moveToFirst();
        do {
            CategoryModel categoryModel;
            categoryModel = new CategoryModel(cursor.getInt(0),
                    cursor.getInt(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getInt(4));
            list.add(categoryModel);

        } while (cursor.moveToNext());
        return list;
    }

    public List<DataModel> getDataFromId(int id) {
        List<DataModel> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM data WHERE categories_id = '" + id + "'", null);
        cursor.moveToFirst();
        do {
            DataModel model;
            model = new DataModel(cursor.getInt(0),
                    cursor.getString(1), cursor.getInt(2),
                    cursor.getInt(3), cursor.getInt(4),
                    cursor.getInt(5));
            list.add(model);
        } while (cursor.moveToNext());
        return list;

    }
}