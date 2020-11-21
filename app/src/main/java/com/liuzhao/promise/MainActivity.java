package com.liuzhao.promise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.imyeego.promise.Fun0;
import com.imyeego.promise.Promise;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private TextView tv1;
    private Button btStart;
    private Button btStop;
    private int count = 2;

    Promise<?> promise;

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        tv1 = findViewById(R.id.tv1);
        btStart = findViewById(R.id.bt_start);
        btStop = findViewById(R.id.bt_stop);
        Promise.of(()->{
           Thread.sleep(4000);
           return "123";
        }).map(Integer::parseInt).then(s -> {
            Log.e("promise then", "" + s);
        }).ui(s -> {
            tv.setText(String.valueOf(s + 7));
            Toast.makeText(MainActivity.this, "" + (s + 7), Toast.LENGTH_SHORT).show();
//            throw new NullPointerException();
            Log.e("promise ui", Thread.currentThread().getName() + ": " + s);
            count = 9;
        }).then(s -> {
            Log.e("promise then", Thread.currentThread().getName() + ": " + count);
        }).excep(e -> {
            e.printStackTrace();
        }).make();

        btStart.setOnClickListener(v -> startForeachByPromise());
        btStop.setOnClickListener(v -> stopForeachPromise());
    }

    private void startForeachByPromise() {
        promise = Promise.forEach(() -> {
            long result = System.currentTimeMillis();
            return dateFormat.format(new Date(result));
        }, 2000).ui(result -> {
            tv1.setText(String.valueOf(result));
        }).make();
    }

    private void stopForeachPromise() {
        promise.cancel();
    }
}
