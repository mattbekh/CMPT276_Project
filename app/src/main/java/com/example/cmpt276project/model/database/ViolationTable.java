package com.example.cmpt276project.model.database;

public class ViolationTable {
    public static final String NAME = "Violations";

    public static final String FIELD_ID = "Id";
    public static final String FIELD_INSPECTION_ID = "InspectionId";
    public static final String FIELD_CODE = "Code";
    public static final String FIELD_DESCRIPTION = "Description";
    public static final String FIELD_IS_CRITICAL = "IsCritical";
    public static final String FIELD_IS_REPEAT = "IsRepeat";

    public static final int COL_ID = 0;
    public static final int COL_INSPECTION_ID = 1;
    public static final int COL_CODE = 2;
    public static final int COL_DESCRIPTION = 3;
    public static final int COL_IS_CRITICAL = 4;
    public static final int COL_IS_REPEAT = 5;

    public static final String[] FIELDS = new String[]{
            FIELD_ID,
            FIELD_CODE,
            FIELD_DESCRIPTION,
            FIELD_IS_CRITICAL,
            FIELD_IS_REPEAT
    };

    public static final String CREATE =
            "CREATE TABLE " + NAME + " (" +
                FIELD_ID + " INT PRIMARY KEY AUTOINCREMENT, " +
                FIELD_INSPECTION_ID + "INT FOREIGN KEY" +
                FIELD_CODE + " INT NOT NULL," +
                FIELD_DESCRIPTION + " TEXT NOT NULL," +
                FIELD_IS_REPEAT + " INT NOT NULL," +
                FIELD_IS_CRITICAL + " INT NOT NULL," +
            ");";
}
