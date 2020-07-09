package com.example.cmpt276project.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;


import com.example.cmpt276project.R;
import com.example.cmpt276project.model.Violation;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This is an ArrayAdapter class to display a violations list in a scrollable ListView
 */
public class ViolationsListAdapter extends ArrayAdapter<Violation> {

    LayoutInflater layout;
    private Context context;

    public ViolationsListAdapter(@NonNull Context context, int resource, ArrayList<Violation> violations){
        super(context, resource, violations);
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Violation violation = getItem(position);

        layout = LayoutInflater.from(context);
        View itemView = layout.inflate(R.layout.violation_row, null);

        ImageView tvIconImage = itemView.findViewById(R.id.iconImage);
        TextView tvDescription = itemView.findViewById(R.id.descriptionText);
        ImageView tvSeverityImage = itemView.findViewById(R.id.severityImage);

        tvDescription.setText(violation.getSummary());

        if (Objects.requireNonNull(violation).isCritical()) {
            tvSeverityImage.setImageResource(R.drawable.unhappy_face_icon);
            tvSeverityImage.setColorFilter(ActivityCompat.getColor(context, R.color.highHazard));
        } else {
            tvSeverityImage.setImageResource(R.drawable.straight_face_icon);
            tvSeverityImage.setColorFilter(ActivityCompat.getColor(context, R.color.lowHazard));
        }

        int iconResourceId = getResourceId(violation.getCategory());
        String description = violation.getDescription();
        String summary = violation.getSummary();

        tvIconImage.setImageResource(iconResourceId);
        itemView.setOnClickListener(new ViolationOnClickListener(iconResourceId, description, summary));

        return itemView;
    }

    @DrawableRes
    private static int getResourceId(Violation.Category category) {
        switch(category) {
            case PERMITS:
                return R.drawable.violations_permit_icon;
            case EMPLOYEES:
                return R.drawable.violations_employee_icon;
            case EQUIPMENT:
                return R.drawable.violations_equipment_icon;
            case PESTS:
                return R.drawable.violations_pest_icon;
            case FOOD:
                return R.drawable.violations_food_icon;
        }
        String errorMessage = String.format("Unhandled category [%s]", category);
        throw new IllegalArgumentException(errorMessage);
    }

    // Class for onClickListener to call and display the ViolationFragment
    private class ViolationOnClickListener implements View.OnClickListener {
        private int iconResourceId;
        private String description;
        private String summary;

        public ViolationOnClickListener(int iconResourceId, String description, String summary) {
            this.iconResourceId = iconResourceId;
            this.description = description;
            this.summary = summary;
        }

        @Override
        public void onClick(View v) {
            FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
            Bundle bundle = new Bundle();

            bundle.putString("description", description );
            bundle.putInt("iconResourceId", iconResourceId );
            bundle.putString("summary", summary);

            ViolationFragment dialog = new ViolationFragment();
            dialog.setArguments(bundle);
            dialog.show(manager, "ViolationDialog");
        }
    }

}
