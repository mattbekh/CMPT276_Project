package com.example.cmpt276project.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {

    // Stores an array of restaurant names (Should be pre-ordered)
    private Context context;
    private RestaurantManager restaurants;

    public static Intent makeIntent(Context context) {
        return new Intent(context, RestaurantListAdapter.class);
    }

    public RestaurantListAdapter(Context ct, RestaurantManager manager){

        context = ct;
        restaurants = manager;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.restaurant_row,parent,false);
        return new MyViewHolder(view);
    }

    // Sets the name to the card according to the position in the recycler view
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int pos = holder.getAdapterPosition();
        // Get Restaurant
        Restaurant restaurant = restaurants.get(pos);
        // Store Restaurant name
        holder.restaurantName_tv.setText(restaurant.getName());

        // Store most recent inspections # of issues
        Inspection topInspection = restaurant.getInspectionByIndex(0);
        if(topInspection.getTrackingNumber() == "EMPTY"){
            holder.inspectionDate_tv.setText("No Inspections Yet");
            holder.numberOfIssues_tv.setText("");
            holder.hazardLevel.setVisibility(View.INVISIBLE);
        }
        else {
            int numCriticalIssues = topInspection.getNumCriticalIssues();
            int numNonCriticalIssues = topInspection.getNumNonCriticalIssues();
            int numIssues = numCriticalIssues + numNonCriticalIssues;
            holder.numberOfIssues_tv.setText("# of Issues : " + numIssues);

            // Store most recent inspections date
            holder.inspectionDate_tv.setText("" + topInspection.getSmartDate());

            // Modify hazard level icon
            switch (topInspection.getHazardRating()) {

                case LOW:
                    holder.hazardLevel.setImageResource(R.drawable.happy_face_icon);
                    break;
                case MODERATE:
                    holder.hazardLevel.setImageResource(R.drawable.straight_face_icon);
                    break;
                case HIGH:
                    holder.hazardLevel.setImageResource(R.drawable.unhappy_face_icon);
                    break;
            }
        }


        holder.restaurantListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =RestaurantActivity.makeIntent(context);

                // put index of Restaurant as extra
                intent.putExtra("restaurant",pos);
                context.startActivity(intent);
            }
        });
    }

    // Need to pass items we have in our array
    @Override
    public int getItemCount() {
        // Change with restaurants.getLength()
        return restaurants.getLength();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView restaurantName_tv;
        TextView numberOfIssues_tv;
        TextView inspectionDate_tv;
        ImageView hazardLevel;

        ConstraintLayout restaurantListLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName_tv = itemView.findViewById(R.id.restaurantName);
            numberOfIssues_tv = itemView.findViewById(R.id.numIssues);
            inspectionDate_tv = itemView.findViewById(R.id.inspectionDate);
            hazardLevel = itemView.findViewById(R.id.hazardIcon);

            restaurantListLayout = itemView.findViewById(R.id.rowLayout);
        }
    }
}
