package com.example.cmpt276project.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;


import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Violation;

import java.util.List;

public class ViolationsListAdapter extends ArrayAdapter<Violation> {

    private LayoutInflater layout;
    private int res;
    private Context context;

    public ViolationsListAdapter(Context ct,int resource, List<Violation> violations){
        super(ct,resource,violations);
        context = ct;
        res = resource;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        layout = LayoutInflater.from(context);
        View itemView = layout.inflate(R.layout.violation_row,null);

        ImageView tvIconImage = itemView.findViewById(R.id.iconImage);
        TextView tvDescription = itemView.findViewById(R.id.descriptionText);
        ImageView tvSeverityImage = itemView.findViewById(R.id.severityImage);

        //place icomImage setters

        tvDescription.setText(getItem(position).getDescription());

        if (getItem(position).isCritical() == true) {
            tvSeverityImage.setImageResource(R.drawable.unhappy_face_icon);
            tvSeverityImage.setColorFilter(ActivityCompat.getColor(context,R.color.mediumHazard));
        }
        else{
            tvSeverityImage.setImageResource(R.drawable.straight_face_icon);
            tvSeverityImage.setColorFilter(ActivityCompat.getColor(context,R.color.highHazard));
        }

        return itemView;
    }

}
