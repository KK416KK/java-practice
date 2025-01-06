package com.websarva.wings.android.yest1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class Dialog_Second extends DialogFragment {

    int number;
    private DatabaseHelper _helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            number = getArguments().getInt("num", -1);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle saveInstance){
        _helper = new DatabaseHelper(requireContext());
        Dialog_Second dialog = new Dialog_Second();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("DATA");
        builder.setMessage("what do you do");
        builder.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(requireContext(), editter.class);
                intent.putExtra("id", number + 1);
                startActivity(intent);
            }
        });
        builder.setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                _helper.deleteDataById(number + 1);
                if (getActivity() != null) {
                    Fragment parentFragment = getActivity().getSupportFragmentManager()
                            .findFragmentById(R.id.fragment_container); // Fragmentを表示する親のID
                    if (parentFragment instanceof aitem_list_frag) {
                        ((aitem_list_frag) parentFragment).loadDataIntoList();
                    }
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }
}
