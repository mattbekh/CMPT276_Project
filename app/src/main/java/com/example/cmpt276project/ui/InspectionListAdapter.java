package com.example.cmpt276project.ui;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

/**
 * This class modifies the InspectionList UI of Inspections
 */
public class InspectionListAdapter extends ArrayAdapter<Inspection> {

    private Context ct;
    private int mResource;


    public InspectionListAdapter(@NonNull Context context, int resource, ArrayList<Inspection> inspections) {
        super(context, resource, inspections);
        ct = context;
        mResource = resource;

    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(ct);

        View view = inflater.inflate(R.layout.inspection_row,null);

        TextView numCritIssues_TV = view.findViewById(R.id.numCritIssues);
        TextView numNonCritIssues_TV = view.findViewById(R.id.numNonCritIssues);
        TextView date_TV = view.findViewById(R.id.inspectionDate);
        ImageView hazardIcon = view.findViewById(R.id.hazardIcon);

        numCritIssues_TV.setText(ct.getString(R.string.num_critical_issues) + getItem(position).getNumCriticalIssues());
        numNonCritIssues_TV.setText(ct.getString(R.string.num_non_critical_issues) + getItem(position).getNumNonCriticalIssues());
        date_TV.setText(ct.getString(R.string.inspection_date) + getItem(position).getSmartDate());

        // Modify hazard level icon
        switch (getItem(position).getHazardRating()) {

            case LOW:
                hazardIcon.setImageResource(R.drawable.happy_face_icon);
                hazardIcon.setColorFilter(ActivityCompat.getColor(ct, R.color.lowHazard));
                break;
            case MODERATE:
                hazardIcon.setImageResource(R.drawable.straight_face_icon);
                hazardIcon.setColorFilter(ActivityCompat.getColor(ct, R.color.mediumHazard));
                break;
            case HIGH:
                hazardIcon.setImageResource(R.drawable.unhappy_face_icon);
                hazardIcon.setColorFilter(ActivityCompat.getColor(ct, R.color.highHazard));
                break;
        }

        return view;
    }
}
