package com.example.cmpt276project.model;

import android.content.res.Resources;

import com.example.cmpt276project.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents an inspection to a restaurant. It contains information regarding to the
 * date that the inspection took place, the type of inspection,  a list of violations that were
 * documented, and counts of critical and non-critical issues.
 */
public class Inspection {

    public enum InspectionType { ROUTINE, FOLLOW_UP }
    public enum HazardRating { LOW, MODERATE, HIGH }

    private ArrayList<Violation> violationList;
    private String trackingNumber;
    private Date date;
    private InspectionType inspectionType;
    private HazardRating hazardRating;
    private int numCriticalIssues;
    private int numNonCriticalIssues;

    public Inspection(String trackingNumber,
                      String inspectionType,
                      String hazardRating,
                      Date date)
    {
        this.trackingNumber = trackingNumber;
        this.date = date;
        this.violationList = new ArrayList<>();
        this.inspectionType = getInspectionTypeEnum(inspectionType);
        this.hazardRating = getHazardRatingEnum(hazardRating);
        this.numCriticalIssues = 0;
        this.numNonCriticalIssues = 0;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public InspectionType getInspectionType() {
        return inspectionType;
    }

    public HazardRating getHazardRating() {
        return hazardRating;
    }

    public int getNumCriticalIssues() {
        return numCriticalIssues;
    }

    public int getNumNonCriticalIssues() {
        return numNonCriticalIssues;
    }

    public ArrayList<Violation> getViolationList() {
        return violationList;
    }

    public String getInspectionTypeString() {

        switch (inspectionType) {
            case ROUTINE:
                return Resources.getSystem().getString(R.string.Inspection_inspection_type_routine);
            case FOLLOW_UP:
                return Resources.getSystem()
                        .getString(R.string.Inspection_inspection_type_follow_up);
            default:
                String errorMessage = String.format("Invalid inspection type [%s]",
                                                    inspectionType.toString());
                throw new IllegalStateException(errorMessage);
        }
    }

    public String getHazardRatingString() {
        switch (hazardRating) {
            case LOW:
                return Resources.getSystem().getString(R.string.Inspection_hazard_rating_low);
            case MODERATE:
                return Resources.getSystem().getString(R.string.Inspection_hazard_rating_moderate);
            case HIGH:
                return Resources.getSystem().getString(R.string.Inspection_hazard_rating_high);
            default:
                String errorMessage = String.format("Invalid inspection type [%s]",
                        inspectionType.toString());
                throw new IllegalStateException(errorMessage);
        }
    }

    public void addViolation(Violation violation) {
        violationList.add(violation);
        if (violation.isCritical()) {
            numCriticalIssues++;
        } else {
            numNonCriticalIssues++;
        }
    }

    private InspectionType getInspectionTypeEnum(String inspectionType) {
        switch (inspectionType.toLowerCase()) {
            case "routine":
                return InspectionType.ROUTINE;
            case "follow-up":
                return InspectionType.FOLLOW_UP;
            default:
                String errorMessage = String.format("Invalid inspectionType [%s]", inspectionType);
                throw new IllegalArgumentException(errorMessage);
        }
    }

    protected HazardRating getHazardRatingEnum(String hazardRating) {
        switch (hazardRating.toLowerCase()) {
            case "low":
                return HazardRating.LOW;
            case "moderate":
                return HazardRating.MODERATE;
            case "high":
                return HazardRating.HIGH;
            default:
                String errorMessage = String.format("Invalid hazardRating [%s]", hazardRating);
                throw new IllegalArgumentException(errorMessage);
        }
    }
}
