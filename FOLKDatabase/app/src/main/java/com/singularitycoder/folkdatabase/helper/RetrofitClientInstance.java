package com.singularitycoder.folkdatabase.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClientInstance {

    // Declare a retrofit instance
    private static Retrofit retrofit;

    // Configure retrofit instance with the Base URL
    private static final String URL = "https://us-central1-folk-demo.cloudfunctions.net/";
    private static OkHttpClient httpClient = new OkHttpClient();
    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static OkHttpClient httpClientBuff() {
        httpClient.readTimeoutMillis();
        httpClient.connectTimeoutMillis();
        return httpClient;
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(URL)
                    .client(httpClientBuff())
//                    .addConverterFactory(GsonConverterFactory.create(gson))     // Convert JSON response easily with this
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
