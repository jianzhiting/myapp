package com.jane.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WeekTwo extends AppCompatActivity implements View.OnClickListener{

    public static String TAG = "two";
    private TextView out;
    private EditText inp;
    private String str = "Halo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week2);

        out = findViewById(R.id.out);
        inp = findViewById(R.id.inp);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG,"onclick");
        str = inp.getText().toString();
        out.setText(str);
    }
}
