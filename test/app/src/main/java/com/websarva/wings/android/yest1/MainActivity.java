package com.websarva.wings.android.yest1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //最初に表示するフラグメント
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments,new homefrag()).commit();

        Button home_button = findViewById(R.id.homes);
        Button data_button = findViewById(R.id.datas);
        Button list_button = findViewById(R.id.items);

        //ボタンを押してフラグメントを張り替える
        home_button.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments,new homefrag()).addToBackStack(null).commit();
        });
        data_button.setOnClickListener(v -> {
            datafrag dfrag = new datafrag();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments, dfrag).addToBackStack(null).commit();
        });
        list_button.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments,new aitem_list_frag()).addToBackStack(null).commit();
        });
    }

}