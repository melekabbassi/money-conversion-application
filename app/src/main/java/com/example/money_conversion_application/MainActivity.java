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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

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
                                try {
                                    res = convertCurrency("USD", "TND", initValue);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            resultTextView.setText(String.valueOf(res));
                            break;
                        case R.id.ConvertToEurosRadioButton:
                            try {
                                res = convertCurrency("EUR", "TND", initValue);
                            } catch (IOException | ParseException e) {
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

    public String convertCurrency(String toCurrency, String fromCurrency, double amount) throws IOException, ParseException {
        String url_str = "https://api.exchangerate.host/convert?from= " + fromCurrency + " &to= " + toCurrency + " &amount= " + amount;

        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        String req_result = jsonobj.get("result").getAsString();
        System.out.println(req_result);
        return req_result;
    }
}