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

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {

    // Stores an array of restaurant names (Should be pre-ordered)
    private Context context;
    private RestaurantManager restaurants;

    // TODO : May need more variables to be passed in
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
        final int pos = position;

        // Store Restaurant name
        holder.restaurantName_tv.setText(restaurants.get(pos).getName());

        // Store most recent inspections # of issues
        Inspection topInspection = restaurants.get(pos).getTopInspection();
        int numCriticalIssues = topInspection.getNumCriticalIssues();
        int numNonCriticalIssues = topInspection.getNumNonCriticalIssues();
        int numIssues = numCriticalIssues + numNonCriticalIssues;
        holder.numberOfIssues_tv.setText("" + numIssues);

        // Store most recent inspections date
        holder.inspectionDate_tv.setText("" +topInspection.getDate());

        // Modify hazard level icon
        if(numCriticalIssues>2){
            holder.hazardLevel.setImageResource(R.drawable.unhappy_face_icon);
        }
        else if(numCriticalIssues==0 && numNonCriticalIssues==0){
            holder.hazardLevel.setImageResource(R.drawable.happy_face_icon);
        }
        else{
            holder.hazardLevel.setImageResource(R.drawable.straight_face_icon);
        }

        holder.restaurantListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantActivity.class);
                Restaurant restaurant = restaurants.get(pos);
                // put a serializable object as extra
                intent.putExtra("restaurant",restaurant);

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
