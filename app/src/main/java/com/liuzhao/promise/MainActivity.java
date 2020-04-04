package com.liuzhao.promise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.imyeego.promise.Fun0;
import com.imyeego.promise.Promise;

public class MainActivity extends AppCompatActivity {
    private TextView tv;
    private int count = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
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


    }
}
