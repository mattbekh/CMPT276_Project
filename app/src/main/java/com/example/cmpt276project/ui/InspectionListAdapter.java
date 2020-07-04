package com.example.cmpt276project.ui;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class InspectionListAdapter extends ArrayAdapter<Inspection> {

    private Context ct;
    private int mResource;
    private Restaurant restaurantSelected;

    public InspectionListAdapter(@NonNull Context context, int resource, ArrayList<Inspection> inspections) {
        super(context, resource, inspections);
        ct = context;
        mResource = resource;

    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        int numCritIssues = getItem(position).getNumCriticalIssues();
        int numNonCritIssues = getItem(position).getNumNonCriticalIssues();

//        Inspection inspection = new Inspection()
//        Inspection inspection = restaurantSelected.getInspectionByIndex(position);
//        int numCritIssues = inspection.getNumCriticalIssues();
        LayoutInflater inflater = LayoutInflater.from(ct);
//        convertView = inflater.inflate(mResource,parent,false);

        View view = inflater.inflate(R.layout.inspection_row,null);

        TextView numCritIssues_TV = view.findViewById(R.id.numCritIssues);
        TextView numNonCritIssues_TV = view.findViewById(R.id.numNonCritIssues);
        TextView date_TV = view.findViewById(R.id.inspectionDate);

        numCritIssues_TV.setText("Number of Critical Issues : "+numCritIssues);
        numNonCritIssues_TV.setText("Number of Non-Critical Issues : "+numNonCritIssues);
        date_TV.setText(getItem(position).getSmartDate());

        return view;
    }
}
