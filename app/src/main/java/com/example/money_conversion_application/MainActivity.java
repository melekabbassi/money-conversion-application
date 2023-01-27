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

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
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
                    String res;

                    initValue = Float.valueOf(editText.getText().toString());

                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.ConvertToDollarsRadioButton:
                            try {
                                res = convertCurrency("USD", "TND", initValue);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            resultTextView.setText(String.valueOf(res));
                            break;
                        case R.id.ConvertToEurosRadioButton:
                            try {
                                res = convertCurrency("EUR", "TND", initValue);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            resultTextView.setText(String.valueOf(res));
                            break;
                        default:
                            break;
                    }
                }
            }
        }));
    }

    public String convertCurrency(String toCurrency, String fromCurrency, double amount) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api.apilayer.com/exchangerates_data/convert?to=" + toCurrency + "&from= " + fromCurrency +"&amount= " + amount)
                .addHeader("apikey", "BxWlwFUUB5gndcZW7jWst2gpkXbiYYGn")
                .method("GET", null)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(Objects.requireNonNull(response.body()).string());
        return Objects.requireNonNull(response.body()).string();
    }
}