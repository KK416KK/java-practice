package com.websarva.wings.android.yest1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class aitem_list_frag extends Fragment implements dialogs.DialogListener {

    ListView listview;
    private DatabaseHelper _helper;
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;

    private final ActivityResultLauncher<Intent> editLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == requireActivity().RESULT_OK) {
                    // データ更新後にリストをリロード
                    loadDataIntoList();
                }
            });

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_aitem_list_frag, container, false);

        listview = view.findViewById(R.id.list_all);
        _helper = new DatabaseHelper(requireActivity());
        items = new ArrayList<>();

        loadDataIntoList();

        //リストビューを長押ししたときダイアログを表示
        listview.setOnItemLongClickListener((parent, v, position, id) -> {
            DialogFragment dialogfragment = new Dialog_Second();

            Bundle args = new Bundle();
            args.putInt("num", (int) id);
            dialogfragment.setArguments(args);

            dialogfragment.show(getActivity().getSupportFragmentManager(), "dialogs");

            return true;

        });
        return view;
    }

    @Override
    public void onEditSelected(int id, String name) {
        Intent intent = new Intent(requireActivity(), editter.class);
        intent.putExtra("id", id);
        //intent.putExtra("name", name);
        editLauncher.launch(intent);
    }

    @Override
    public void onDeleteSelected(int id) {
        SQLiteDatabase db = _helper.getWritableDatabase();
        db.delete("inputer", "_id=?", new String[]{String.valueOf(id)});
        db.close();
        loadDataIntoList();
    }

    public void loadDataIntoList(){
        String[][] data = _helper.getProductsData();
        items.clear();

        for (String[] row : data) {
            items.add(row[2] + " - " + row[3]); // Display item name and money
        }

        adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, items);
        listview.setAdapter(adapter);
    }
    private int getIdFromItem(String selectedItem) {
        // アイテム名からIDを取得するロジックを実装
        String[][] data = _helper.getProductsData();
        for (String[] row : data) {
            String itemName = row[2] + " - " + row[3];
            if (itemName.equals(selectedItem)) {
                return Integer.parseInt(row[0]); // ID を返す
            }
        }
        return -1; // 見つからなかった場合
    }
}
