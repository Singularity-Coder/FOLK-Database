package com.singularitycoder.folkdatabase.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClientInstance {

    private static final String TAG = "RetrofitClientInstance";
    private static final String URL = "https://us-central1-folk-demo.cloudfunctions.net/";  // Configure retrofit instance with the Base URL

    private static Retrofit _instance;   // Declare a retrofit instance
    private static Gson gson = new GsonBuilder().setLenient().create();

    private static OkHttpClient.Builder getHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    requestBuilder.addHeader("Content-Type", "application/json");
                    requestBuilder.addHeader("Accept", "application/json");
                    return chain.proceed(requestBuilder.build());
                });
    }

    public static Retrofit getInstance() {
        if (_instance == null) {
            _instance = new Retrofit
                    .Builder()
                    .client(getHttpClientBuilder().build())
                    .baseUrl(URL)
//                    .addConverterFactory(GsonConverterFactory.create(gson))     // Convert JSON response easily with this
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return _instance;
    }
}
