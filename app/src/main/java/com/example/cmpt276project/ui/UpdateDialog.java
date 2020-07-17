package com.example.cmpt276project.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.cmpt276project.R;

public class UpdateDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.test_layout, null);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Log.i("TestFrag","Clickity Positive");

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Log.i("TestFrag","Clickity Negative");
                        getActivity().finish();
                        break;
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle("Update Available, download data?")
                .setView(v)
                .setPositiveButton("Yes",listener)
                .setNegativeButton("No",listener)
                .create();
    }
}
