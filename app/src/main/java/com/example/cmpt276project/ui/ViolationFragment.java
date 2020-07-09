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

/**
 * This class creates a message fragment which displays information on a specific violation
 */
public class ViolationFragment extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.violation_fragment_layout,null);
        initializeFragment(view);

        // Create a button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No additional functionality
            }
        };

        // Build the alert dialog
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok,listener)
                .create();
    }

    private void initializeFragment(View view) {
        // Find resource IDs
        TextView sumTV = view.findViewById(R.id.violationSummary);
        TextView violationScripTV = view.findViewById(R.id.violationScrip);
        ImageView violationIcon = view.findViewById(R.id.violationIcon);

        // Get data
        Bundle bundle = this.getArguments();
        int iconResourceId = bundle.getInt("iconResourceId");
        String myDescription = bundle.getString("description");
        String mySum = bundle.getString("summary");

        // Set data
        violationIcon.setImageResource(iconResourceId);
        violationScripTV.setText(myDescription);
        sumTV.setText(mySum);
    }
}
