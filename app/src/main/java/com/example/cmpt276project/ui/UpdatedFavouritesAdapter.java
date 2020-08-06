package com.example.cmpt276project.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.cmpt276project.model.database.DatabaseManager;
import com.example.cmpt276project.model.database.RestaurantFilter;

import java.util.ArrayList;

public class UpdatedFavouritesAdapter extends RecyclerView.Adapter<UpdatedFavouritesAdapter.MyViewHolder>{

    private Context context;
    private RestaurantManager manager;

    public static Intent makeIntent(Context context) {
        return new Intent(context, RestaurantListAdapter.class);
    }

    public UpdatedFavouritesAdapter(Context context, RestaurantManager manager){
        this.context = context;
        this.manager = manager;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.restaurant_row,parent,false);
        return new UpdatedFavouritesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpdatedFavouritesAdapter.MyViewHolder holder, int position) {
        final Restaurant restaurant = manager.getRestaurantList().get(position);
        DatabaseManager dbManager = DatabaseManager.getInstance();

        //Fix: filter restaurants by ones with new inspections
        if(restaurant.isFavourite()) {
            holder.restaurantName.setText(restaurant.getName());
            holder.restaurantAddress.setText(restaurant.getAddress() + ", " + restaurant.getCity());

            int iconResource = getResourceID(restaurant.getRestaurantName());
            holder.restaurantIcon.setImageResource(iconResource);
            holder.favouritesIcon.setImageResource(R.drawable.star_icon);

            Inspection topInspection = dbManager.getMostRecentInspection(restaurant.getId());
            if (topInspection == null) {
                holder.inspectionDate.setText(R.string.Inspection_no_inspections_found);
                holder.numberOfIssues.setText("");
                holder.hazardLevel.setVisibility(View.INVISIBLE);
            } else {
                int numCriticalIssues = topInspection.getNumCriticalIssues();
                int numNonCriticalIssues = topInspection.getNumNonCriticalIssues();
                int numIssues = numCriticalIssues + numNonCriticalIssues;
                holder.numberOfIssues.setText(context.getString(R.string.Inspection_num_of_issues) + numIssues);

                // Store most recent inspections date
                holder.inspectionDate.setText(context.getString(R.string.restaurantList_most_recent_inspect) + topInspection.getSmartDate());

                // Modify hazard level icon
                switch (topInspection.getHazardRating()) {
                    case LOW:
                        holder.hazardLevel.setImageResource(R.drawable.happy_face_icon);
                        holder.hazardLevel.setColorFilter(ActivityCompat.getColor(context, R.color.lowHazard));
                        break;
                    case MODERATE:
                        holder.hazardLevel.setImageResource(R.drawable.straight_face_icon);
                        holder.hazardLevel.setColorFilter(ActivityCompat.getColor(context, R.color.mediumHazard));
                        break;
                    case HIGH:
                        holder.hazardLevel.setImageResource(R.drawable.unhappy_face_icon);
                        holder.hazardLevel.setColorFilter(ActivityCompat.getColor(context, R.color.highHazard));
                        break;
                }
            }
        }
        else{
            holder.restaurantIcon.setImageResource(0);
            holder.hazardLevel.setImageResource(0);
        }

    }

    @DrawableRes
    private static int getResourceID(Restaurant.RestaurantName restaurantName){
        switch(restaurantName) {
            case MCDONALDS:
                return R.drawable.restaurant_mcdonalds_logo;
            case WENDYS:
                return R.drawable.restaurant_wendys_logo;
            case BLENZ:
                return R.drawable.restaurant_blenz_logo;
            case PIZZAHUT:
                return R.drawable.restaurant_pizzahut_logo;
            case AW:
                return R.drawable.restaurant_aw_logo;
            case TIMS:
                return R.drawable.restaurant_tims_logo;
            case STARBUCKS:
                return R.drawable.restaurant_starbucks_logo;
            case ELEVEN:
                return R.drawable.restaurant_eleven_logo;
            case BOSTON:
                return R.drawable.restaurant_boston_logo;
            case SUBWAY:
                return R.drawable.restaurant_subway_logo;
            case UNKNOWN:
                return R.drawable.fork_spoon_icon;
        }
        String errorMessage = String.format("Unhandled category [%s]", restaurantName);
        throw new IllegalArgumentException(errorMessage);
    }

    @Override
    public int getItemCount() {
        return manager.getRestaurantList().size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder {

        TextView restaurantName;
        TextView restaurantAddress;
        TextView numberOfIssues;
        TextView inspectionDate;
        ImageView hazardLevel;
        ImageView restaurantIcon;
        ImageView favouritesIcon;

        ConstraintLayout restaurantListLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.restaurantName = itemView.findViewById(R.id.restaurantName);
            this.restaurantAddress = itemView.findViewById(R.id.restaurantAdrs);
            this.numberOfIssues = itemView.findViewById(R.id.numIssues);
            this.inspectionDate = itemView.findViewById(R.id.inspectionDate);
            this.hazardLevel = itemView.findViewById(R.id.hazardIcon);
            this.restaurantIcon = itemView.findViewById(R.id.restaurantIcon);
            this.favouritesIcon = itemView.findViewById(R.id.favIcon);

            restaurantListLayout = itemView.findViewById(R.id.rowLayout);
        }
    }
}
