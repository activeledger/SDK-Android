package com.example.activeledgersdk.API;



import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;



public interface APIService {

    @Headers("Content-Type: application/json")
    @POST(".")
    Observable<String> sendTransaction(@Body String post);



}