package com.example.cmpt276project.model;

/**
 * This class represents an individual violation that can be cited in an inspection of a restaurant
 */
public class Violation {

    private int id;
    private boolean isCritical;
    private boolean isRepeat;
    private String description;

    public Violation(int id,
                     boolean isCritical,
                     boolean isRepeat,
                     String description)
    {
        this.id = id;
        this.isCritical = isCritical;
        this.isRepeat = isRepeat;
        this.description = description;
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
}
