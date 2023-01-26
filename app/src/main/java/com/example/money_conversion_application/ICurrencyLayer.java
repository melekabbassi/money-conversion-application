package com.example.money_conversion_application;

import java.io.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.*;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ICurrencyLayer {
    public static final String API_URL = "https://api.apilayer.com/currency_data/convert";
    public static final String API_KEY = "BxWlwFUUB5gndcZW7jWst2gpkXbiYYGn";

    @Headers("apikey: " + API_KEY)
    @GET(API_URL)
    Call<String> convertCurrency(@Query("to") String toCurrency, @Query("from") String fromCurrency, @Query("amount") double amount);
}
