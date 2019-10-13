package com.mgosu.imagepreview.data.api.network;

import com.mgosu.imagepreview.data.api.ImageCategory.DataCategoryImage;
import com.mgosu.imagepreview.data.api.database.DataAll;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiSever {
    @GET("dev/stt.php")
    Call<DataAll> getDatabase(@Query("type") String type);

    @GET("dev/stt.php")
    Call<DataCategoryImage> getImageCategory(@Query("type") String type);
}
