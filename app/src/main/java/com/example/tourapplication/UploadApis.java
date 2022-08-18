package com.example.tourapplication;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApis {
    @Multipart
    @POST("store_photo")
    Call<AddPhoto> uploadImage(
            @Part MultipartBody.Part photo ,
    @Part("email") RequestBody email

            );

}
