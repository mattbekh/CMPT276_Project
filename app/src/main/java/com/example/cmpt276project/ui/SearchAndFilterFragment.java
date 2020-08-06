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
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.database.RestaurantFilter;

import java.util.HashSet;
import java.util.logging.Level;

public class SearchAndFilterFragment extends AppCompatDialogFragment {
    private RestaurantManager manager;
    private boolean isFavourites;
    private TextView nameView;
    private TextView issueView;
    private EditText nameInput;
    private EditText issueMin;
    private EditText issueMax;
    private Switch favourites;
    private CheckBox lowLevel;
    private CheckBox midLevel;
    private CheckBox highLevel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_filter_fragment, null);

        // Find resource IDs
        nameView = (TextView) view.findViewById(R.id.SF_nameTextView);
        issueView = (TextView) view.findViewById(R.id.SF_issueTextView);
        nameInput = (EditText) view.findViewById(R.id.SF_nameInput);
        issueMin = (EditText) view.findViewById(R.id.SF_issueMin);
        issueMax = (EditText) view.findViewById(R.id.SF_issueMax);
        favourites = (Switch) view.findViewById(R.id.SF_favouritesSwitch);
        lowLevel = (CheckBox) view.findViewById(R.id.SF_Low_checkBox);
        midLevel = (CheckBox) view.findViewById(R.id.SF_Moderate_checkBox);
        highLevel = (CheckBox) view.findViewById(R.id.SF_High_checkBox);

        // set hint
        nameInput.setHint("ex: pizza");
        issueMin.setHint("Min");
        issueMax.setHint("Max");

        setFilterValues();
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
                 dismiss();
             }
         };

        DialogInterface.OnClickListener clearListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RestaurantFilter.setFilter(null, null, null, null, null);
                Activity parentActivity = getActivity();
                dismiss();
                ((UpdateFilterListener) parentActivity).updateFilter();
            }
        };

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(R.string.SF_ok, searchListener)
                .setNegativeButton(R.string.SF_clear, clearListener)
                .setNeutralButton(R.string.SF_cancel, cancelListener)
                .create();

        // Build Dialog
        return alertDialog;
    }

    private void setFilterValues() {
        RestaurantFilter filter = RestaurantFilter.getInstance();

        HashSet<Inspection.HazardRating> hazardRatings = filter.getHazardRatings();
        if (hazardRatings == null) {
            lowLevel.setChecked(true);
            midLevel.setChecked(true);
            highLevel.setChecked(true);
        } else {
            if (hazardRatings.contains(Inspection.HazardRating.LOW)) {
                lowLevel.setChecked(true);
            }
            if (hazardRatings.contains(Inspection.HazardRating.MODERATE)) {
                midLevel.setChecked(true);
            }
            if (hazardRatings.contains(Inspection.HazardRating.HIGH)) {
                highLevel.setChecked(true);
            }
        }

        String name = filter.getName();
        nameInput.setText(name);

        Integer minCritical = filter.getMinCritical();
        Integer maxCritical = filter.getMaxCritical();
        if (minCritical != null) {
            issueMin.setText(String.format("%d", minCritical));
        }
        if (maxCritical != null) {
            issueMax.setText(String.format("%d", maxCritical));
        }

        Integer onlyFavourites = filter.isFavouritesOnly();
        if (onlyFavourites != null && onlyFavourites == 1) {
            favourites.setChecked(true);
        }


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