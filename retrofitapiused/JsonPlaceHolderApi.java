package com.example.retrofitapiused;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET("api/visitor/current/health/issue/{user_id}")
    Call<String> getPosts(@Path("user_id") String name);

    @GET("api/visitor/current/health/issue/{user_id}")
    Call<model> getIssue(@Path("user_id") String user_id);
}