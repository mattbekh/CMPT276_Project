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
import android.widget.ListView;
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

public class UpdatedFavouritesFragment extends AppCompatDialogFragment {
    private RestaurantManager manager;
    private boolean isFavourites;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_updated_favourites_fragment, null);

        // Find resource IDs
        TextView nameView = (TextView) view.findViewById(R.id.text_updatedFavs);
        ListView restaurantList = (ListView) view.findViewById(R.id.list_Favourites);



        // button listener
        DialogInterface.OnClickListener InfoListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };


        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, InfoListener)
                .create();

        // Build Dialog
        return alertDialog;
    }

}