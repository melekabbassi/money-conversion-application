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
                    float initValue = 0.0f, res;

                    initValue = Float.valueOf(editText.getText().toString());

                    switch (radioGroup.getCheckedRadioButtonId()) {
                        case R.id.ConvertToDollarsRadioButton:
                            res = tndToUsd(initValue);
                            resultTextView.setText(String.valueOf(res));
                            break;
                        case R.id.ConvertToEurosRadioButton:
                            res = tndToEur(initValue);
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
}