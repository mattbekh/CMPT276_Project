package com.example.cmpt276project.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.cmpt276project.R;

import java.util.StringTokenizer;

public class ViolationFragment extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        // Load message layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.violation_fragment_layout,null);

        // Update message fragment
        updateFragment(view);

        // Create a button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        // Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok,listener)
                .create();
    }

    private void updateFragment(View view) {
        // Find resource IDs
        TextView dateTV = view.findViewById(R.id.violationDate);
        TextView violationScripTV = view.findViewById(R.id.violationScrip);
        ImageView violationIcon = view.findViewById(R.id.violationIcon);

        // Get Data
        Bundle bundle = this.getArguments();

        // Set the Data
        int iconResourceId = bundle.getInt("iconResourceId");
        violationIcon.setImageResource(iconResourceId);

        // String myDate = bundle.getString("date");
        // dateTV.setText(getString(R.string.violation_date) + myDate);

        String myDescription = bundle.getString("description");
        violationScripTV.setText(myDescription);
    }
}
