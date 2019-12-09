package com.liuzhao.promise;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.imyeego.promise.Promise;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Promise.of(()->{
           Thread.sleep(4000);
           return "promise";
        }).then(s -> {
            // do something
        }).ui(s -> {
            // ui option
        }).make();




    }
}
