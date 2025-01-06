package com.websarva.wings.android.yest1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class editter extends AppCompatActivity {
    private DatabaseHelper _helper;
    private EditText edte1, edte2,edte3,edte4;
    private Spinner spinner;
    private Button button,button2;
    private TextView text1;
    private RadioGroup rdzeikin;
    private String selectedItem;
    private int kt1 = -1;
    private int rdb = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editter);

        Intent intent = getIntent();
        int ids = intent.getIntExtra("id",-1);

        _helper = new DatabaseHelper(editter.this);
        SQLiteDatabase db = _helper.getWritableDatabase();
        String sql = "SELECT * FROM inputer WHERE _id = ?";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(ids)});

        edte1 = findViewById(R.id.edtext2);
        edte2 = findViewById(R.id.edhinmoku2);
        edte3 = findViewById(R.id.edmoney2);
        edte4 = findViewById(R.id.edcompany2);

        rdzeikin = findViewById(R.id.rdzei2);

//        if (cursor.getCount() == 0) {
//            Log.d("EditActivity", "No data found for ID: " + ids);
//        } else {
//            Log.d("EditActivity", "Data found for ID: " + ids);
//        }

        if (cursor.moveToFirst()) {
            edte1.setText(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            edte2.setText(cursor.getString(cursor.getColumnIndexOrThrow("item")));
            edte3.setText(cursor.getString(cursor.getColumnIndexOrThrow("money")));
            edte4.setText(cursor.getString(cursor.getColumnIndexOrThrow("price")));
        }
        cursor.close();

        List<String> asse = Arrays.asList("現金・預金","売掛金","受取手形","有価証券","在庫","前払費用","固定資産","減価償却資産","貸付金","敷金・保証金","その他資産");
        List<String> dept = Arrays.asList("買掛金","支払手形","借入金","未払費用","前受収益","預り金","未払い税金","その他負債");
        List<String> worth  = Arrays.asList("資本金","資本剰余金","自己株式","利益剰余金","その他純資産");
        List<String> reve = Arrays.asList("売上高","営業外収益","特別利益","その他収益");
        List<String> cost = Arrays.asList("仕入高","給与・人件費","広告宣伝費","地代家賃","支払利息","減価償却費","旅費交通費","租税公課","雑費","販売費・一般管理費","その他費用");

        spinner = findViewById(R.id.spinnerkan2);

        edte1.setOnClickListener(view -> showDatePickerDialog());
        //スピナーのアイテムを取得する
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (String) adapterView.getItemAtPosition(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                kt1 = 0;
                if (selectedItem == null || selectedItem.isEmpty()) {
                    text1.setText("勘定科目を選択してください");
                    return;
                }
            }
        });

        //ラジオボタンの内容を取得
        rdzeikin.setOnCheckedChangeListener((group, checkedId) -> {
            rdb = checkedId;
        });

        String updateQuery = "UPDATE inputer " +
                "SET date = ?, item = ?, money = ?, zeikin = ?, price = ?, kanjo = ?, kanjot = ? " +
                "WHERE _id = ?";
        SQLiteStatement stmt = db.compileStatement(updateQuery);

        button = findViewById(R.id.button_2);
        button2 = findViewById(R.id.button_3);

        text1 = findViewById(R.id.text2);

        //ボタンを押したときの操作
        button.setOnClickListener(new View.OnClickListener() {
            //資産、負債などの何れに当たるを記録する
            @Override
            public void onClick(View view) {
                if (asse.contains(selectedItem)){
                    kt1 = 1;
                } else if (dept.contains(selectedItem)) {
                    kt1 = 2;
                } else if (worth.contains(selectedItem)) {
                    kt1 = 3;
                } else if (reve.contains(selectedItem)) {
                    kt1 = 4;
                } else if (cost.contains(selectedItem)){
                    kt1 = 5;
                }else {
                    kt1 = -1;
                }
                //全て入力されている時、データを保存する
                if(kt1 < 0 || edte1.getText().toString().isEmpty() || edte2.getText().toString().isEmpty() || edte3.getText().toString().isEmpty() || rdb == -1 || edte4.getText().toString().isEmpty()){
                    text1.setText("入力が完全ではありません");
                }else{
                    db.beginTransaction();

                    try {
                        stmt.bindString(1, edte1.getText().toString());
                        stmt.bindString(2, edte2.getText().toString());
                        stmt.bindLong(3, Integer.parseInt(edte3.getText().toString()));
                        stmt.bindString(4, String.valueOf(rdb));
                        stmt.bindString(5, edte4.getText().toString());
                        stmt.bindString(6, selectedItem);
                        stmt.bindLong(7, kt1);
                        stmt.bindLong(8, ids);
                        stmt.executeUpdateDelete();
                        db.setTransactionSuccessful();

                        edte2.setText("");
                        edte3.setText("");
                        Toast.makeText(getApplicationContext(), "登録しました", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        //何らかの理由で失敗したとき
                        Toast.makeText(getApplicationContext(), "登録に失敗しました", Toast.LENGTH_LONG).show();
                    } finally {
                        db.endTransaction();
                    }
                }
            }
        });
        button2.setOnClickListener(view -> finish());
    }

    private void showDatePickerDialog(){
        Calendar calender = Calendar.getInstance();
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) ->
        {
            String selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;

            edte1.setText(selectedDate);
        },year,month,day);

        datePickerDialog.show();
    }

    @Override
    protected void onDestroy(){
        _helper.close();
        super.onDestroy();
    }
}
