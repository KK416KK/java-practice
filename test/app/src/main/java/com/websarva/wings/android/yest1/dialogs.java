package com.websarva.wings.android.yest1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class dialogs extends DialogFragment {

    public interface DialogListener{
        void onEditSelected(int id, String name);
        void onDeleteSelected(int id);
    }

    public DialogListener listener;
    private int ids;
    private String names;

    public static dialogs newInstance(int id, String name) {
        dialogs dialog = new dialogs();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("name", name);
        dialog.setArguments(args);
        return dialog;
    }

    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        Fragment parentFragment = getParentFragment();

        if (parentFragment != null && parentFragment instanceof DialogListener) {
            listener = (DialogListener) parentFragment;
        } else if (context instanceof DialogListener) {
            listener = (DialogListener) context;
        } else {
            throw new RuntimeException("Parent fragmentまたはアクティビティはDialogListenerを実装する必要があります");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        if (getArguments() != null) {
            ids = getArguments().getInt("id");
            names = getArguments().getString("name");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("what do you do");
        builder.setItems(new String[] {"EDIT","DELETE","CANCEL"},(dialog,which) -> {
           switch (which){
               case 0:
                   //EDIT
                   if (listener != null){
                       listener.onEditSelected(ids,names);
                   }
                   break;
               case 1:
                   //DLETE
                   if(listener != null){
                       listener.onDeleteSelected(ids);
                   }
                   break;
               case 2:
                   //CANCEL
                   dialog.dismiss();
                   break;
           }
        });
        return builder.create();
    }
}
