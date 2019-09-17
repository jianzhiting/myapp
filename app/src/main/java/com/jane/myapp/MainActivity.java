package com.jane.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ImageView mImg;
    private int mIndex = 0;
    private int picture[] = new int[]{
        R.mipmap.cake0,
        R.mipmap.cake1,
        R.mipmap.cake2,
        R.mipmap.cake3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImg = (ImageView) findViewById(R.id.cake);
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIndex = (1 + mIndex)%4;
                mImg.setImageResource(picture[mIndex]);
                Toast.makeText(MainActivity.this, R.string.toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
