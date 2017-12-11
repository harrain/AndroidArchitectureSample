package com.example.rxjava2start;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by net on 2017/12/8.
 */

public class RetrofitBuilderHelper {

    private static Retrofit retrofit;

    public static Retrofit getInstance(){
        if (retrofit == null) {
            synchronized(RetrofitBuilderHelper.class){
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl("http://fy.iciba.com/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
