package com.example.cmpt276project.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.airbnb.lottie.parser.IntegerParser;
import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.database.RestaurantFilter;

import java.util.HashSet;
import java.util.logging.Level;

public class SearchAndFilterFragment extends AppCompatDialogFragment {
    private RestaurantManager manager;
    private boolean isFavourites;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_filter_fragment, null);

        // Find resource IDs
        TextView nameView = (TextView) view.findViewById(R.id.SF_nameTextView);
        TextView issueView = (TextView) view.findViewById(R.id.SF_issueTextView);
        EditText nameInput = (EditText) view.findViewById(R.id.SF_nameInput);
        EditText issueMin = (EditText) view.findViewById(R.id.SF_issueMin);
        EditText issueMax = (EditText) view.findViewById(R.id.SF_issueMax);
        Switch favourites = (Switch) view.findViewById(R.id.SF_favouritesSwitch);
        CheckBox lowLevel = (CheckBox) view.findViewById(R.id.SF_Low_checkBox);
        CheckBox midLevel = (CheckBox) view.findViewById(R.id.SF_Moderate_checkBox);
        CheckBox highLevel = (CheckBox) view.findViewById(R.id.SF_High_checkBox);

        // set hint
        nameInput.setHint("ex: pizza");
        issueMin.setHint("Min");
        issueMax.setHint("Max");
        lowLevel.setChecked(true);
        midLevel.setChecked(true);
        highLevel.setChecked(true);

        favouritesSwitch(favourites);

        // button listener
        DialogInterface.OnClickListener searchListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameInput.getText().toString();
                if (name.equals("")) {
                    name = null;
                }

                String minCriticalString = issueMin.getText().toString();
                String maxCriticalString = issueMax.getText().toString();
                Integer minCritical;
                Integer maxCritical;
                try {
                    minCritical = Integer.parseInt(minCriticalString);
                } catch (Exception e){
                    minCritical = null;
                }
                try {
                    maxCritical = Integer.parseInt(maxCriticalString);
                } catch (Exception e) {
                    maxCritical = null;
                }

                HashSet<Inspection.HazardRating> hazardRatings = new HashSet<>();
                if (lowLevel.isChecked()) {
                    hazardRatings.add(Inspection.HazardRating.LOW);
                }
                if (midLevel.isChecked()) {
                    hazardRatings.add(Inspection.HazardRating.MODERATE);
                }
                if (highLevel.isChecked()) {
                    hazardRatings.add(Inspection.HazardRating.HIGH);
                }

                RestaurantFilter.setFilter(name, hazardRatings, minCritical, maxCritical, isFavourites);
                Activity parentActivity = getActivity();
                ((UpdateFilterListener) parentActivity).updateFilter();
            }
        };

         DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 RestaurantFilter.setFilter(null, null, null, null, null);
                 Activity parentActivity = getActivity();
                 ((UpdateFilterListener) parentActivity).updateFilter();
             }
         };

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.search_go, searchListener)
                .setNegativeButton(android.R.string.cancel, cancelListener)
                .create();

        // Build Dialog
        return alertDialog;
    }


    private void favouritesSwitch(Switch s) {
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isFavourites = true;
                } else {
                    isFavourites = false;
                }
            }
        });
    }

    public interface UpdateFilterListener {
        void updateFilter();
    }
}