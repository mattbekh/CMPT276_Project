package com.example.cmpt276project.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.DateHelper;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.Violation;
import com.example.cmpt276project.model.database.DatabaseManager;
import com.example.cmpt276project.model.database.RestaurantFilter;

import java.util.ArrayList;
import java.util.Objects;

public class UpdatedFavouritesAdapter extends ArrayAdapter<Restaurant> {

    LayoutInflater layout;
    private Context context;

    public UpdatedFavouritesAdapter(@NonNull Context context, int resource, ArrayList<Restaurant> favourites) {
        super(context, resource, favourites);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Restaurant restaurant = getItem(position);

        layout = LayoutInflater.from(context);
        View itemView = layout.inflate(R.layout.updatedfavourites_row, null);

        TextView restaurantName = itemView.findViewById(R.id.restaurantName);
        TextView restaurantAddress = itemView.findViewById(R.id.restaurantAdrs);
        TextView numIssues = itemView.findViewById(R.id.numIssues);
        TextView inspectionDate = itemView.findViewById(R.id.inspectionDate);

        restaurantName.setText(restaurant.getName());
        restaurantAddress.setText(restaurant.getAddress());

        DatabaseManager dbManager = DatabaseManager.getInstance();
        Inspection topInspection = dbManager.getMostRecentInspection(restaurant.getId());
        if (topInspection == null) {
            inspectionDate.setText(R.string.Inspection_no_inspections_found);
            numIssues.setText("");

        } else {
            int numCriticalIssues = topInspection.getNumCriticalIssues();
            int numNonCriticalIssues = topInspection.getNumNonCriticalIssues();
            int numOfIssues = numCriticalIssues + numNonCriticalIssues;
            numIssues.setText(context.getString(R.string.Inspection_num_of_issues) + numOfIssues);


            // Store most recent inspections date
            inspectionDate.setText(context.getString(R.string.restaurantList_most_recent_inspect) + topInspection.getSmartDate());
        }

        return itemView;
    }
}
