package com.websarva.wings.android.yest1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.database.Cursor;
import android.widget.Button;
import android.widget.TextView;


public class homefrag extends Fragment {

    private DatabaseHelper _helper;
    private int total = 0;
    private TextView textview;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _helper = new DatabaseHelper(getActivity());
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_homefrag, container, false);

        textview = view.findViewById(R.id.assets);
        button = view.findViewById(R.id.input_button);
        Intent intent = new Intent(getActivity(),input.class);

        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = _helper.getWritableDatabase();
            String sql = "SELECT SUM(price) AS total FROM inputer WHERE kanjot = ?";
            cursor = db.rawQuery(sql, new String[]{String.valueOf(1)});

            // 資産の合計を計算する
            if (cursor.moveToFirst()) {
                int columnin = cursor.getColumnIndex("total");
                if (columnin != -1) {
                    total = cursor.getInt(columnin);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        textview.setText(String.valueOf(total));

        cursor.close();
        db.close();

        //入力画面への画面遷移
        button.setOnClickListener(v ->{
            startActivity(intent);
        });
        return  view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_helper != null) {
            _helper.close();
        }
    }
}
