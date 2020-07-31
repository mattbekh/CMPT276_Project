package com.example.cmpt276project.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.database.RestaurantFilter;

public class SearchAndFilterFragment extends AppCompatDialogFragment {
    private RestaurantManager manager;
    private RestaurantFilter filter;
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

        // set hint
        nameInput.setHint("ex: pizza");
        issueMin.setHint("Min");
        issueMax.setHint("Max");

        favouritesSwitch(favourites);

        // button listener
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //TODO: how to know if editText is null
                // get data from edit text view
//                if(nameInput.getText() == null) {
//                    String name = null;
//                    Toast.makeText(getContext(), "name is null", Toast.LENGTH_SHORT).show();
//                } else if(issueMin.getText() == null) {
//                    int minCritical = Integer.parseInt(null);
//                    Toast.makeText(getContext(), "Min Critical is null", Toast.LENGTH_SHORT).show();
//                } else if(issueMax.getText() == null) {
//                    int maxCritical = Integer.parseInt(null);
//                    Toast.makeText(getContext(), "Max Critical is null", Toast.LENGTH_SHORT).show();
//                } else {
//                    String name = nameInput.getText().toString();
//                    int minCritical = Integer.parseInt(issueMin.getText().toString());
//                    int maxCritical = Integer.parseInt(issueMax.getText().toString());
//                    if(minCritical > maxCritical) {
//                        Toast.makeText(getContext(), "Min Critical > Max Critical", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getContext(), "pass", Toast.LENGTH_SHORT).show();
//                    }
//                }
                String name = nameInput.getText().toString();
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
                RestaurantFilter.setFilter(name,null,minCritical,maxCritical,isFavourites);
                Activity parentActivity = getActivity();
                ((UpdateFilterListener) parentActivity).updateFilter();
                // TODO: how to use applyFilter
//                manager.applyFilter();
            }
        };

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, listener)
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