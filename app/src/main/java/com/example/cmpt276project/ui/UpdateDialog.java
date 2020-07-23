package com.example.cmpt276project.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.update_dialog, null);

        // Set up the buttons of the dialog
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int buttonPressed) {
                dismiss();
                Activity parentActivity = getActivity();
                if (parentActivity instanceof UpdateDialogListener) {
                    if (buttonPressed == DialogInterface.BUTTON_POSITIVE) {
                        ((UpdateDialogListener) parentActivity).downloadData();
                    } else if (buttonPressed == DialogInterface.BUTTON_NEGATIVE) {
                        ((UpdateDialogListener) parentActivity).setUpCluster();
                    }
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.UpdateDialog_title))
                .setView(v)
                .setPositiveButton("Yes",listener)
                .setNegativeButton("No",listener)
                .create();
    }

    public interface UpdateDialogListener {
        void downloadData();
        void setUpCluster();
    }
}
