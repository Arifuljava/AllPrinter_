package com.messas.cpclprintersdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(MainActivity.this);

    }
}