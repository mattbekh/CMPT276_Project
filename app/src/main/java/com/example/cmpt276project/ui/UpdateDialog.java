package com.example.cmpt276project.ui;

import android.app.Dialog;
import android.content.Context;
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

    private MyStringListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.update_dialog, null);

        // Set up the buttons of the dialog
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch(which){

                    // If user clicks the OK button, launch the attached function from main thread
                    case DialogInterface.BUTTON_POSITIVE:
                        // Pass a true boolean which then tells program to download data
                        mListener.downloadCSVData(true);

//                finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // Pass false boolean which cancels download
                        mListener.downloadCSVData(false);
                        //getActivity().finish();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (MyStringListener) context;

        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    public interface MyStringListener{
        public void downloadCSVData(boolean b);
    }
}
