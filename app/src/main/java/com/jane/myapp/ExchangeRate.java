package com.jane.myapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ExchangeRate extends AppCompatActivity implements Runnable{

    public static String TAG = "here";
    private TextView out;
    private EditText inp;
    private Float f;
    private Float dollarRate;
    private Float euroRate;
    private Float wonRate;
    private Intent config;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bd = (Bundle) msg.obj;

            switch (msg.what){
                case 1:
                    dollarRate = bd.getFloat("dollar_rate_save_key", 0.1408f);
                    euroRate = bd.getFloat("euro_rate_save_key", 0.1278f);
                    wonRate = bd.getFloat("won_rate_save_key", 168.0834f);
                    Toast.makeText(ExchangeRate.this, R.string.updateRate, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.i("here", "message: HandlerClass default");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_rate);

        SharedPreferences sp = getSharedPreferences("myrate", MODE_PRIVATE);
        dollarRate = sp.getFloat("dollarRate_key", 0.1408f);
        euroRate = sp.getFloat("euroRate_key", 0.1278f);
        wonRate = sp.getFloat("wonRate_key", 168.0834f);

        out = findViewById(R.id.out);
        inp = findViewById(R.id.inp);

        Button btn2dollar = findViewById(R.id.btn2dollar);
        Button btn2euro = findViewById(R.id.btn2euro);
        Button btn2won = findViewById(R.id.btn2won);

        btn2dollar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inp.getText().toString().equals("")){
                    f = Float.parseFloat(inp.getText().toString())*dollarRate;
                    out.setText(String.format("%.2f", f) + "$");
                }
            }
        });
        btn2euro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inp.getText().toString().equals("")) {
                    f = Float.parseFloat(inp.getText().toString()) * euroRate;
                    out.setText(String.format("%.2f", f) + "€");
                }
            }
        });
        btn2won.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inp.getText().toString().equals("")) {
                    f = Float.parseFloat(inp.getText().toString()) * wonRate;
                    out.setText(String.format("%.2f", f) + "₩");
                }
            }
        });

        Button btnconfig = findViewById(R.id.btnconfig);
        config = new Intent(this, ExchangeRateConfig.class);

        btnconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfigPage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        new Thread(){
            @Override
            public void run() {
//                URL url = null;
                Float dollar_rate = 0.1408f;
                Float euro_rate = 0.1278f;
                Float won_rate = 168.0834f;

                try {
//                    url = new URL("http://www.usd-cny.com/bankofchina.htm");
//                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
//                    InputStream in = http.getInputStream();
//                    String html = inputStream2String(in);

//                    File input = new File("d:/input.html");
//                    Document doc = Jsoup.parse(input, "UTF-8", "http://www.usd-cny.com/bankofchina.htm");

                    String url = "http://www.usd-cny.com/";
                    Document doc = Jsoup.connect(url).get();
                    Elements tds = doc.select("tbody > tr > td");

                    for(int i=0; i<tds.size(); i+=5){
                        Element td1 = tds.get(i);
                        String str1 = td1.text();
                        int cnt = 1;
                        String str2 = "-";
                        while (str2.equals("-")){
                            str2 = tds.get(cnt+i).text();
                            cnt++;
                        }
                        Log.i(TAG, "run: " + str1 + " " + str2);
                        if(str1.equals("美元"))
                            dollar_rate = 100f/Float.parseFloat(str2);
                        if(str1.equals("欧元"))
                            euro_rate = 100f/Float.parseFloat(str2);
                        if(str1.equals("韩币"))
                            won_rate = 100f/Float.parseFloat(str2);
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }

                Bundle bd = new Bundle();
                bd.putFloat("dollar_rate_save_key", dollar_rate);
                bd.putFloat("euro_rate_save_key", euro_rate);
                bd.putFloat("won_rate_save_key", won_rate);

                SharedPreferences sp = getSharedPreferences("myrate", MODE_PRIVATE);
                SharedPreferences.Editor ed = sp.edit();
                ed.putFloat("dollarRate_key", dollarRate);
                ed.putFloat("euroRate_key", euroRate);
                ed.putFloat("wonRate_key", wonRate);
                ed.commit();

                Message msg = mHandler.obtainMessage(1);
                msg.obj = bd;
                mHandler.sendMessage(msg);
            }
        }.start();

//        Thread t = new Thread(this);
//        t.start();

        return super.onOptionsItemSelected(item);
    }

    private String inputStream2String(InputStream in) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader rd = new InputStreamReader(in, "gb2312");
        while (true){
            int rsz = rd.read(buffer, 0, buffer.length);
            if(rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    private void openConfigPage() {
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);

        startActivityForResult(config, 1);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        Bundle bd = data.getExtras();
        dollarRate = bd.getFloat("dollar_rate_save_key", 0.1408f);
        euroRate = bd.getFloat("euro_rate_save_key", 0.1278f);
        wonRate = bd.getFloat("won_rate_save_key", 168.0834f);
    }

    public void run(){
        Log.i(TAG, "run");
    }
}
