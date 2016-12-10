package com.google.android.gms.samples.vision.barcodereader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Hamza Mounir on 30-11-16.
 */
public interface ApiRoutes {

    @POST("/connect/mobile")
    @FormUrlEncoded
    Call<ResponseBody> getUser(@Field("login") String login);

    @POST("/command/add")
    @FormUrlEncoded
    Call<ResponseBody> addProduct(@Field("login") String login, @Field("command") String command, @Field("product") String product, @Field("quantity") String quantity);

    @GET("/command/new/{login}")
    Call<ResponseBody> newCommand(@Path("login") String login);

    @GET("/command/last/{login}")
    Call<ResponseBody> getCommand(@Path("login") String login);



}
