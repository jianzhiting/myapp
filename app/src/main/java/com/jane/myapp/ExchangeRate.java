package com.jane.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ExchangeRate extends AppCompatActivity{

    public static String TAG = "two";
    private TextView out;
    private EditText inp;
    private Float f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_rate);

        out = findViewById(R.id.out);
        inp = findViewById(R.id.inp);

        Button btn2dollar = findViewById(R.id.btn2dollar);
        Button btn2euro = findViewById(R.id.btn2euro);
        Button btn2won = findViewById(R.id.btn2won);

        btn2dollar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f = Float.parseFloat(inp.getText().toString())*0.1408f;
                out.setText(String.format("%.2f", f) + "$");
            }
        });
        btn2euro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f = Float.parseFloat(inp.getText().toString())*0.1278f;
                out.setText(String.format("%.2f", f) + "€");
            }
        });
        btn2won.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f = Float.parseFloat(inp.getText().toString())*168.0834f;
                out.setText(String.format("%.2f", f) + "₩");
            }
        });
    }
}
