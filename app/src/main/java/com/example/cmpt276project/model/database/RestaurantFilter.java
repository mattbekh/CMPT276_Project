package com.example.cmpt276project.model.database;

import com.example.cmpt276project.model.DateHelper;
import com.example.cmpt276project.model.RestaurantManager;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import static com.example.cmpt276project.model.Inspection.HazardRating;

public class RestaurantFilter {

    private String name;
    private HashSet<HazardRating> hazardRatings;
    private Integer minCritical;
    private Integer maxCritical;
    private Integer onlyFavourites;

    private static RestaurantFilter instance;

    private RestaurantFilter() {
        this.name = null;
        this.hazardRatings = null;
        this.minCritical = null;
        this.maxCritical = null;
        this.onlyFavourites = null;
    }

    public static RestaurantFilter getInstance() {
        if (instance == null) {
            instance = new RestaurantFilter();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public HashSet<HazardRating> getHazardRatings() {
        return hazardRatings;
    }

    private static String getHazardRating(HazardRating hazardRating) {
        if (hazardRating != null) {
            switch (hazardRating) {
                case LOW:
                    return "Low";
                case MODERATE:
                    return "Moderate";
                case HIGH:
                    return "High";
            }
        }
        return null;
    }

    private String getHazardRatingsString() {
        if (hazardRatings == null) {
            return null;
        }
        if (hazardRatings.size() == 0) {
            return ("()");
        }

        StringBuilder ratings = new StringBuilder("(");
        for (HazardRating rating : hazardRatings) {
            ratings.append("'").append(getHazardRating(rating)).append("',");
        }

        // Replace trailing ',' with ')'
        // ex: "(Low,Moderate," -> "(Low,Moderate)"
        ratings.setCharAt(ratings.length() - 1, ')');

        return ratings.toString();
    }

    public Integer getMinCritical() {
        return minCritical;
    }

    public Integer getMaxCritical() {
        return maxCritical;
    }

    public Integer isFavouritesOnly() {
        return onlyFavourites;
    }

    public static void setFilter(
            String name,
            HashSet<HazardRating> hazardRatings,
            Integer minCritical,
            Integer maxCritical,
            Boolean isFavouritesOnly
    ) {
        if (instance == null) {
            instance = new RestaurantFilter();
        }

        Integer onlyFavourites;
        if (isFavouritesOnly == null) {
            onlyFavourites = null;
        } else if (isFavouritesOnly) {
            onlyFavourites = 1;
        } else {
            onlyFavourites = 0;
        }
        if (hazardRatings == null){

        } else if (hazardRatings.size() == 3) {
            hazardRatings = null;
        }

        if (name != instance.name
            || hazardRatings != instance.hazardRatings
            || minCritical != instance.minCritical
            || maxCritical != instance.maxCritical
            || onlyFavourites != instance.onlyFavourites)
        {
            instance.name = name;
            instance.hazardRatings = hazardRatings;
            instance.minCritical = minCritical;
            instance.maxCritical = maxCritical;
            instance.onlyFavourites = onlyFavourites;
            RestaurantManager.getInstance().applyFilter();
        }
    }

    public static String getFilterSelectionCriteria() {
        if (instance == null) {
            instance = new RestaurantFilter();
        }

        ArrayList<String> selectionCriteria = new ArrayList<>();
        StringBuilder selection = new StringBuilder();

        if (instance.getName() != null) {
            selectionCriteria.add(RestaurantTable.FIELD_NAME + " LIKE '%" + instance.getName() + "%'");
        }
        if (instance.isFavouritesOnly() != null) {
            selectionCriteria.add(RestaurantTable.FIELD_IS_FAVOURITE + " = " + instance.isFavouritesOnly());
        }
        if (instance.getHazardRatingsString() != null) {
            selectionCriteria.add(getHazardRatingCriteria());
        }

        String numCriticalCriteria = getNumCriticalCriteria();
        if (numCriticalCriteria != null) {
            selectionCriteria.add(numCriticalCriteria);
        }

        if (selectionCriteria.size() == 0) {
            return null;
        }

        for (int i = 0; i < selectionCriteria.size(); i++) {
            selection.append(selectionCriteria.get(i));
            if (i < selectionCriteria.size() - 1) {
                selection.append(" AND ");
            }
        }

        return selection.toString();
    }

    private static String getHazardRatingCriteria() {
        String hazardRatings = RestaurantFilter.getInstance().getHazardRatingsString();
        String criteria;
        if (hazardRatings.equals("()")) {
            criteria =
                RestaurantTable.FIELD_ID + " NOT IN (" +
                    "SELECT DISTINCT " + InspectionTable.FIELD_RESTAURANT_ID + " " +
                    "FROM " + InspectionTable.NAME +
                ")";
        } else {
            criteria =
                RestaurantTable.FIELD_ID + " IN (" +
                    "SELECT " + InspectionTable.FIELD_RESTAURANT_ID + " " +
                    "FROM (" +
                        "SELECT " +
                            InspectionTable.FIELD_RESTAURANT_ID + ", " +
                            InspectionTable.FIELD_DATE + ", " +
                            InspectionTable.FIELD_HAZARD_RATING + ", " +
                            "MAX (" + InspectionTable.FIELD_DATE + ") " +
                        "FROM " + InspectionTable.NAME + " " +
                        "GROUP BY " + InspectionTable.FIELD_RESTAURANT_ID + " " +
                        "HAVING " + InspectionTable.FIELD_DATE + " = MAX (" + InspectionTable.FIELD_DATE + ") " +
                    ") " +
                    "WHERE " + InspectionTable.FIELD_HAZARD_RATING + " IN " + hazardRatings +
                ")";
        }
        return criteria;
    }

    private static String getNumCriticalCriteria() {
        String selectionCriteria = null;
        Integer minCritical = instance.getMinCritical();
        Integer maxCritical = instance.getMaxCritical();

        if (minCritical != null && maxCritical != null) {
            selectionCriteria =
                    "SUM (" + InspectionTable.FIELD_CRITICAL_ISSUES + ") >= " + minCritical + " " +
                            "AND SUM (" + InspectionTable.FIELD_CRITICAL_ISSUES + ") <= " + maxCritical + " ";
        } else if (minCritical != null) {
            selectionCriteria = "SUM (" + InspectionTable.FIELD_CRITICAL_ISSUES + ") >= " + minCritical + " ";
        } else if (maxCritical != null) {
            selectionCriteria = "SUM (" + InspectionTable.FIELD_CRITICAL_ISSUES + ") <= " + maxCritical + " ";
        } else {
            return null;
        }

        String currDate = DateHelper.getDateString(new GregorianCalendar());

        String criteria =
                RestaurantTable.FIELD_ID + " IN (" +
                    "SELECT " + InspectionTable.FIELD_RESTAURANT_ID + " " +
                    "FROM (" +
                        "SELECT " +
                            InspectionTable.FIELD_RESTAURANT_ID + ", " +
                            InspectionTable.FIELD_CRITICAL_ISSUES + ", " +
                            "SUM (" + InspectionTable.FIELD_CRITICAL_ISSUES + ") " +
                        "FROM (" +
                            "SELECT " +
                            InspectionTable.FIELD_RESTAURANT_ID + ", " +
                            InspectionTable.FIELD_CRITICAL_ISSUES + " "  +
                            "FROM (" +
                                "SELECT " +
                                    InspectionTable.FIELD_RESTAURANT_ID + ", " +
                                    InspectionTable.FIELD_CRITICAL_ISSUES + ", " +
                                    InspectionTable.FIELD_DATE + " " +
                                "FROM " + InspectionTable.NAME + " " +
                                "WHERE (" + currDate + " - " + InspectionTable.FIELD_DATE + ") < 10000" +
                            ") " +
                        ") " +
                        "GROUP BY " + InspectionTable.FIELD_RESTAURANT_ID + " " +
                        "HAVING " + selectionCriteria +
                    ") " +
                ")";

        return criteria;
    }

    public static String getUpdatedFavouritesSelection(String previousModifyDate) {
        String criteria =
                RestaurantTable.FIELD_ID + " IN (" +
                    "SELECT " + InspectionTable.FIELD_RESTAURANT_ID + " " +
                    "FROM (" +
                        "SELECT " +
                            InspectionTable.FIELD_RESTAURANT_ID + ", " +
                            "MAX(" + InspectionTable.FIELD_DATE + ") " +
                        "FROM " + InspectionTable.NAME + " " +
                        "GROUP BY " + InspectionTable.FIELD_RESTAURANT_ID + " " +
                        "HAVING MAX(" + InspectionTable.FIELD_DATE + ") > " + previousModifyDate +
                    ") " +
                ") ";
        return criteria;
    }
}
