package com.websarva.wings.android.yest1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class datafrag extends Fragment {

    private DatabaseHelper _helper;
    private int total = 0;
    private String[] all_data;
    private int[] datas = new int[6];
    private TextView text1,text2,text3,text4,text5;
    private TextView text6,text7;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _helper = new DatabaseHelper(getActivity());
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_datafrag, container, false);
        SQLiteDatabase db = _helper.getWritableDatabase();

        text1 = view.findViewById(R.id.assets_1);
        text2 = view.findViewById(R.id.assets);
        text3 = view.findViewById(R.id.assets_2);
        text4 = view.findViewById(R.id.assets_5);
        text5 = view.findViewById(R.id.assets_3);

        text6 = view.findViewById(R.id.assets_4);
        text7 = view.findViewById(R.id.assets_6);

        String sql = "SELECT SUM(IFNULL(price, 0)) AS total FROM inputer WHERE kanjot = ?";

        //変数にデータを入れる
        for (int i = 1;i <= 5;i++){
            Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(i)});
            if(cursor.moveToFirst()){
                int columnin = cursor.getColumnIndex("total");
                if(columnin != -1){
                    total = cursor.getInt(columnin);
                }else{
                    //error_cccn
                }
            }
            datas[i] = total;
            total = 0;
            cursor.close();
        }
        text1.setText(String.valueOf(datas[1]));
        text2.setText(String.valueOf(datas[2]));
        text3.setText(String.valueOf(datas[3]));
        text4.setText(String.valueOf(datas[4]));
        text5.setText(String.valueOf(datas[5]));

        //純利益、純損失を表示する
        if(datas[4] < datas[5]){
            int y = datas[5] - datas[4];
            text7.setText(y);
            text6.setText("-");
        }else if(datas[4] > datas[5]){
            int y = datas[4] - datas[5];
            text6.setText(y);
            text7.setText("-");
        }else {
            text6.setText("-");
            text7.setText("-");
        }
        db.close();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (_helper != null) {
            _helper.close();
        }
    }
}