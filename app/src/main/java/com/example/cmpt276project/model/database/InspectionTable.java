
package com.example.cmpt276project.model.database;

/**
 * This class contains information relation to the inspections table in the database
 */
public class InspectionTable {

        public static final String NAME = "Inspections";

        public static final String FIELD_ID = "Id";
        public static final String FIELD_RESTAURANT_ID = "RestaurantId";
        public static final String FIELD_DATE = "Date";
        public static final String FIELD_TYPE = "Type";
        public static final String FIELD_HAZARD_RATING = "HazardRating";
        public static final String FIELD_CRITICAL_ISSUES = "CriticalIssues";
        public static final String FIELD_NON_CRITICAL_ISSUES = "NonCriticalIssues";

        public static final int COL_ID = 0;
        public static final int COL_RESTAURANT_ID = 1;
        public static final int COL_DATE = 2;
        public static final int COL_TYPE = 3;
        public static final int COL_HAZARD_RATING = 4;
        public static final int COL_CRITICAL_ISSUES = 5;
        public static final int COL_NON_CRITICAL_ISSUES = 6;

        public static final String[] FIELDS = new String[] {
                FIELD_ID,
                FIELD_RESTAURANT_ID,
                FIELD_DATE,
                FIELD_TYPE,
                FIELD_HAZARD_RATING,
                FIELD_CRITICAL_ISSUES,
                FIELD_NON_CRITICAL_ISSUES
        };

        public static final String CREATE =
                "CREATE TABLE " + NAME + " (" +
                    FIELD_ID + " INT PRIMARY KEY," +
                    FIELD_RESTAURANT_ID + " TEXT NOT NULL," +
                    FIELD_DATE + " INT NOT NULL," +
                    FIELD_TYPE + " TEXT NOT NULL," +
                    FIELD_HAZARD_RATING + " TEXT NOT NULL," +
                    FIELD_CRITICAL_ISSUES + " INT NOT NULL," +
                    FIELD_NON_CRITICAL_ISSUES + " INT NOT NULL," +
                    "FOREIGN KEY (" + FIELD_RESTAURANT_ID + ") REFERENCES " + RestaurantTable.NAME +
                ");";
}
