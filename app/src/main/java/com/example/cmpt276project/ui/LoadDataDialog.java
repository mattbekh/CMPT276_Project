package com.example.cmpt276project.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.cmpt276project.R;

public class LoadDataDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.load_data_dialog, null);

        DialogInterface.OnDismissListener listener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Activity parentActivity = getActivity();
                if (parentActivity instanceof LoadDataDialog.OnDismissListener) {
//                    ((LoadDataDialog.OnDismissListener) parentActivity).addRestaurantMarkers();
                }
            }
        };

        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.LoadDataDialog_title))
                .setView(v)
                .setOnDismissListener(listener)
                .create();
    }

    public interface OnDismissListener {
        void addRestaurantMarkers();
    }
}
