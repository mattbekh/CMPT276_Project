package com.example.cmpt276project.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents an individual violation that can be cited in an inspection of a restaurant
 */
public class Violation {

    public enum Category {PERMITS, EMPLOYEES, EQUIPMENT, PESTS, FOOD}

    private int id;
    private boolean isCritical;
    private boolean isRepeat;
    private String description;
    private Category category;

    public Violation(int id,
                     boolean isCritical,
                     boolean isRepeat,
                     String description) {
        this.id = id;
        this.isCritical = isCritical;
        this.isRepeat = isRepeat;
        this.description = description;
        this.category = getCategoryFromDescription(description);
    }

    public int getId() {
        return id;
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

    public Category getCategory() {
        return category;
    }

    public String getSummary() {
        switch (category) {
            case PERMITS:
                return "Permit/operation issue";
            case EMPLOYEES:
                return "Employee/operator issue";
            case EQUIPMENT:
                return "Equipment/utensil/workstation issue";
            case PESTS:
                return "Pest issue";
            case FOOD:
                return "Food sanitation issue";
            default:
                String errorMessage = String.format("Illegal category [%s]", category.toString());
                throw new IllegalStateException(errorMessage);
        }
    }

    private Category getCategoryFromDescription(String description) {
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
                return ".*((permit)|(operation)|(premise)).*";
            case EMPLOYEES:
                return ".*((employee)|(operator)).*";
            case EQUIPMENT:
                return ".*((equipment)|(utensil)|(surface)|(chemical)).*";
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
