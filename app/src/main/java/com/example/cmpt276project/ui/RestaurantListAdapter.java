package com.example.cmpt276project.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cmpt276project.R;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {

    // Stores an array of restaurant names (Should be pre-ordered)
    private String restaurantNames[];
    private Context context;

    // TODO : May need more variables to be passed in
    public RestaurantListAdapter(Context ct, String names[]){

        context = ct;
        restaurantNames = names;

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
        holder.restaurantName_tv.setText(restaurantNames[position]);
    }

    // Need to pass items we have in our array
    @Override
    public int getItemCount() {
        return restaurantNames.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView restaurantName_tv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName_tv = itemView.findViewById(R.id.restaurantName);
        }
    }
}
