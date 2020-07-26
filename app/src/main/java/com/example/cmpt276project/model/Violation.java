package com.example.cmpt276project.model;

import android.content.res.Resources;

import com.example.cmpt276project.App;
import com.example.cmpt276project.R;

/**
 * This class represents an individual violation that can be cited in an inspection of a restaurant
 */
public class Violation {

    public enum Category { PESTS, PERMITS, EQUIPMENT, EMPLOYEES, FOOD }

    private int code;
    private boolean isCritical;
    private boolean isRepeat;
    private String description;

    public Violation(int code,
                     boolean isCritical,
                     boolean isRepeat,
                     String description)
    {
        this.code = code;
        this.isCritical = isCritical;
        this.isRepeat = isRepeat;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public boolean isCritical() {
        return isCritical;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public String getDescription() {
        return description;
    }

    public String getSummary() {
        Resources res = App.resources();
        Category category = getCategory();
        switch (category) {
            case PERMITS:
                return res.getString(R.string.Violation_permit_text);
            case EMPLOYEES:
                return res.getString(R.string.Violation_employee_text);
            case EQUIPMENT:
                return res.getString(R.string.Violation_equipment_text);
            case PESTS:
                return res.getString(R.string.Violation_pest_text);
            case FOOD:
                return res.getString(R.string.Violation_food_text);
            default:
                String errorMessage = String.format("Illegal category [%s]", category.toString());
                throw new IllegalStateException(errorMessage);
        }
    }

    public Category getCategory() {
        description = description.toLowerCase();
        for (Category category : Category.values()) {
            if (description.matches(getKeywordPattern(category))) {
                return category;
            }
        }
        String errorMessage = String.format("Failing description [%s]", description);
        throw new IllegalArgumentException(errorMessage);
    }

    private static String getKeywordPattern(Category category) {
        switch (category) {
            case PERMITS:
                return ".*((permit)|(operation)|(plan)).*";
            case EMPLOYEES:
                return ".*((employee)|(operator)).*";
            case EQUIPMENT:
                return ".*((equipment)|(utensil)|(surface)|(chemical)|(station)).*";
            case PESTS:
                return ".*((pest)|(animal)).*";
            case FOOD:
                return ".*(food).*";
            default:
                String errorMessage = String.format("Illegal category [%s]", category.toString());
                throw new IllegalStateException(errorMessage);
        }
    }
}
