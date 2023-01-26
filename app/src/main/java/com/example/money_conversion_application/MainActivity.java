package com.example.money_conversion_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity implements ICurrencyLayer{
    EditText editText;
    RadioGroup radioGroup;
    Button convertButton;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.MoneyEditText);
        radioGroup = (RadioGroup)findViewById(R.id.ConversionOptionRadioGroup);
        convertButton = (Button)findViewById(R.id.ConvertButton);
        resultTextView = (TextView)findViewById(R.id.ResultTextView);

        convertButton.setOnClickListener((new View.OnClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("Empty Field");
                    alertDialog.setMessage("Please enter a value to convert");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }
                    );
                    alertDialog.show();
                } else {
                    double initValue = 0.0;
                    Call<String> res;

                    initValue = Float.valueOf(editText.getText().toString());

                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.ConvertToDollarsRadioButton:
                            //res = tndToUsd(initValue);
                            res = convertCurrency("USD", "TND", initValue);
                            resultTextView.setText(String.valueOf(res));
                            break;
                        case R.id.ConvertToEurosRadioButton:
                            //res = tndToEur(initValue);
                            res = convertCurrency("EUR", "TND", initValue);
                            resultTextView.setText(String.valueOf(res));
                            break;
                        default:
                            break;
                    }
                }
            }
        }));
    }

    public float tndToUsd(float vTND) {
        return (float)(vTND / 3.09f);
    }

    public float tndToEur(float vTND) {
        return (float)(vTND / 3.37f);
    }

//    public static final String API_URL = "https://api.apilayer.com/currency_data/convert";
//    public static final String API_KEY = "BxWlwFUUB5gndcZW7jWst2gpkXbiYYGn";

//    public static double convertCurrency1(double amount, String fromCurrency, String toCurrency){
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url(API_URL + "?to=" + toCurrency + "&from=" + fromCurrency + "&amount=" + amount)
//                .addHeader("apikey", API_KEY)
//                .method("GET", null)
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            if(response.isSuccessful()){
//                String result = response.body().string();
//                // code to parse the response and return the converted value
//                return Double.parseDouble(result);
//            } else {
//                return 0;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
    public static final String API_URL = "https://api.apilayer.com/currency_data/convert/";
    public static final String API_KEY = "Copy your API key here";

    @Headers("apikey: " + API_KEY)
    @GET(API_URL)
    @Override
    public Call<String> convertCurrency(@Query("to") String toCurrency, @Query("from") String fromCurrency, @Query("amount") double amount) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ICurrencyLayer currencyLayer = retrofit.create(ICurrencyLayer.class);

        Call<String> res = currencyLayer.convertCurrency(toCurrency, fromCurrency, amount);
        res.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();
                    // code to parse the response and return the converted value
                    resultTextView.setText(result);
                } else {
                    resultTextView.setText("0");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                resultTextView.setText("0");
            }
        });
        return res;
    }
}