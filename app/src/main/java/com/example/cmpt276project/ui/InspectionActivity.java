package com.example.cmpt276project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.cmpt276project.R;

public class InspectionActivity extends AppCompatActivity {

    public static Intent makeIntent(Context context) {
        return new Intent(context, InspectionActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);
    }
}