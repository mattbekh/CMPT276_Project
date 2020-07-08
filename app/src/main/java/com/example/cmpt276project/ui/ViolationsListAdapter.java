package com.example.cmpt276project.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Violation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViolationsListAdapter extends ArrayAdapter<Violation> {

    LayoutInflater layout;
    private int res;
    private Context context;
    private int violationIcon = 0;

    public ViolationsListAdapter(@NonNull Context ct, int resource, ArrayList<Violation> violations){
        super(ct,resource,violations);
        context = ct;
        res = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        layout = LayoutInflater.from(context);
        View itemView = layout.inflate(R.layout.violation_row, null);

        ImageView tvIconImage = itemView.findViewById(R.id.iconImage);
        TextView tvDescription = itemView.findViewById(R.id.descriptionText);
        ImageView tvSeverityImage = itemView.findViewById(R.id.severityImage);

        switch(getItem(position).getCategory()){
            case PERMITS:
                tvIconImage.setImageResource(R.drawable.violations_permit_icon);
                violationIcon = 0;
            case EMPLOYEES:
                tvIconImage.setImageResource(R.drawable.violations_employee_icon);
                violationIcon = 1;
            case EQUIPMENT:
                tvIconImage.setImageResource(R.drawable.violations_equipment_icon);
                violationIcon = 2;
            case PESTS:
                tvIconImage.setImageResource(R.drawable.violations_pest_icon);
                violationIcon = 3;
            case FOOD:
                tvIconImage.setImageResource(R.drawable.violations_food_icon);
                violationIcon = 4;
        }

        tvDescription.setText(getItem(position).getDescription());

        if (Objects.requireNonNull(getItem(position)).isCritical()) {
            tvSeverityImage.setImageResource(R.drawable.unhappy_face_icon);
            tvSeverityImage.setColorFilter(ActivityCompat.getColor(context, R.color.highHazard));
        } else {
            tvSeverityImage.setImageResource(R.drawable.straight_face_icon);
            tvSeverityImage.setColorFilter(ActivityCompat.getColor(context, R.color.lowHazard));
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
                Bundle bundle = new Bundle();

                String myDescription = getItem(position).getDescription();

                bundle.putString("description", myDescription );
                bundle.putInt("icon", violationIcon );

                ViolationFragment dialog = new ViolationFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "ViolationDialog");

            }
        });

        return itemView;

    }

}
