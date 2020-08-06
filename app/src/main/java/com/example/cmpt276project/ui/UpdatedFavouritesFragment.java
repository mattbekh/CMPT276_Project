package com.example.cmpt276project.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.Violation;
import com.example.cmpt276project.model.database.DatabaseManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;

public class UpdatedFavouritesFragment extends AppCompatDialogFragment {

    private RestaurantManager manager;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_updated_favourites_fragment, null);

        // Find resource IDs
        TextView nameView = (TextView) view.findViewById(R.id.text_updatedFavs);
        ListView restaurantList = (ListView) view.findViewById(R.id.list_Favourites);

        DatabaseManager dbManager = DatabaseManager.getInstance();
        ArrayList<Restaurant> favourites = ((MapsActivity)getActivity()).getUpdatedFavourites();
        if(favourites != null) {
            ArrayAdapter<Restaurant> adapter = new ArrayAdapter<>(getActivity(), R.layout.restaurant_row, favourites);
            restaurantList.setAdapter(adapter);
        }

        // button listener
        DialogInterface.OnClickListener InfoListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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