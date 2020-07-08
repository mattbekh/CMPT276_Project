package com.example.cmpt276project.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents an individual violation that can be cited in an inspection of a restaurant
 */
public class Violation {
    public enum Category { PERMITS, EMPLOYEES, EQUIPMENT, PESTS, FOOD }
    private int id;
    private boolean isCritical;
    private boolean isRepeat;
    private String description;
    private Category category;
    private HashMap<Category, ArrayList<String>> keywords;
    public Category[] theCategories = { Category.PERMITS, Category.EMPLOYEES, Category.EQUIPMENT, Category.PESTS, Category.FOOD};
    public Violation(int id,
                     boolean isCritical,
                     boolean isRepeat,
                     String description)
    {
        this.id = id;
        this.isCritical = isCritical;
        this.isRepeat = isRepeat;
        this.description = description;
        this.keywords = new HashMap<>();
        populateCategoryKeywordMap();
        this.category = generateCategory(description);
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
    private void populateCategoryKeywordMap() {
        keywords.put(Category.PERMITS, new ArrayList<String>());
        keywords.get(Category.PERMITS).add(".*permit.*");
        keywords.get(Category.PERMITS).add(".*operation.*");
        keywords.get(Category.PERMITS).add(".*premise.*");
        keywords.put(Category.EMPLOYEES, new ArrayList<String>());
        keywords.get(Category.EMPLOYEES).add(".*employee.*");
        keywords.get(Category.EMPLOYEES).add(".*operator.*");
        keywords.put(Category.EQUIPMENT, new ArrayList<String>());
        keywords.get(Category.EQUIPMENT).add(".*equipment.*");
        keywords.get(Category.EQUIPMENT).add(".*utensil.*");
        keywords.get(Category.EQUIPMENT).add(".*surface.*");
        keywords.get(Category.EQUIPMENT).add(".*chemical.*");
        keywords.put(Category.PESTS, new ArrayList<String>());
        keywords.get(Category.PESTS).add(".*pest.*");
        keywords.get(Category.PESTS).add(".*animal.*");
        keywords.put(Category.FOOD, new ArrayList<String>());
        keywords.get(Category.FOOD).add(".*food.*");
    }
    private Category generateCategory(String fullDescription) {
        fullDescription = fullDescription.toLowerCase();
        for (Category c : theCategories) {
            for (String keyword : keywords.get(c)) {
                if (fullDescription.matches(keyword)) {
                    return c;
                }
            }
        }
        String errorMessage = String.format("Failing description [%s]", fullDescription);
        throw new IllegalArgumentException(errorMessage);
    }
}
