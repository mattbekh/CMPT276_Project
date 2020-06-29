package com.example.cmpt276project;

import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Violation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class InspectionTest {

    private static String TRACKING_NUMBER = "ABC123";
    private static String VALID_INSPECTION_TYPE = "Routine";
    private static String INVALID_INSPECTION_TYPE = "Ruotine";
    private static String VALID_HAZARD_RATING = "Low";
    private static String INVALID_HAZARD_RATING = "Loooow";
    private static String VIOLATION_DESCRIPTION = "They violated stuff";
    private static int VIOLATION_ID = 101;

    private Inspection sut;
    private Inspection earlierInspection;
    private Inspection laterInspection;
    private Violation criticalViolation;
    private Violation nonCriticalViolation;

    @Before
    public void beforeEachTest() {
        sut = new Inspection(TRACKING_NUMBER,
                             VALID_INSPECTION_TYPE,
                             VALID_HAZARD_RATING,
                             new Date());
        earlierInspection = new Inspection(TRACKING_NUMBER,
                                           VALID_INSPECTION_TYPE,
                                           VALID_HAZARD_RATING,
                                           new Date(100000));
        laterInspection = new Inspection(TRACKING_NUMBER,
                                         VALID_INSPECTION_TYPE,
                                         VALID_HAZARD_RATING,
                                         new Date(200000));

        criticalViolation = new Violation(VIOLATION_ID, true, false, VIOLATION_DESCRIPTION);
        nonCriticalViolation = new Violation(VIOLATION_ID, false, false, VIOLATION_DESCRIPTION);
    }

    @Test
    public void shouldIncreaseNumCriticalIssuesWhenCriticalViolationAdded() {
        sut.addViolation(criticalViolation);
        assertEquals(sut.getNumCriticalIssues(), 1);
    }

    @Test
    public void shouldNotIncreaseNumNonCriticalIssuesWhenCriticalViolationAdded() {
        sut.addViolation(criticalViolation);
        assertEquals(sut.getNumNonCriticalIssues(), 0);
    }

    @Test
    public void shouldIncreaseNumNonCriticalIssuesWhenNonCriticalViolationAdded() {
        sut.addViolation(nonCriticalViolation);
        assertEquals(sut.getNumNonCriticalIssues(), 1);
    }

    @Test
    public void shouldNotIncreaseNumCriticalIssuesWhenNonCriticalViolationAdded() {
        sut.addViolation(nonCriticalViolation);
        assertEquals(sut.getNumCriticalIssues(), 0);
    }

    @Test
    public void shouldThrowExceptionIfConstructorPassedInvalidInspectionType() {
        shouldThrowExceptionIfConstructorPassedInvalidArgument(INVALID_INSPECTION_TYPE,
                                                               VALID_HAZARD_RATING);
    }

    @Test
    public void shouldThrowExceptionIfConstructorPassedInvalidHazardRating() {
        shouldThrowExceptionIfConstructorPassedInvalidArgument(VALID_INSPECTION_TYPE,
                                                               INVALID_HAZARD_RATING);
    }

    @Test
    public void shouldReturnCorrectValuesForDateAscendingComparator() {
        ArrayList<Inspection> inspectionList = new ArrayList<>();
        inspectionList.add(laterInspection);
        inspectionList.add(earlierInspection);
        inspectionList.sort(new Inspection.DateAscendingComparator());

        assertEquals(earlierInspection, inspectionList.get(0));
        assertEquals(laterInspection, inspectionList.get(1));
    }

    @Test
    public void shouldReturnCorrectValuesForDateDescendingComparator() {
        ArrayList<Inspection> inspectionList = new ArrayList<>();
        inspectionList.add(earlierInspection);
        inspectionList.add(laterInspection);
        inspectionList.sort(new Inspection.DateDescendingComparator());

        assertEquals(laterInspection, inspectionList.get(0));
        assertEquals(earlierInspection, inspectionList.get(1));
    }

    private void shouldThrowExceptionIfConstructorPassedInvalidArgument(String inspectionType,
                                                                        String hazardRating) {
        Exception exception = null;
        try {
            sut = new Inspection(TRACKING_NUMBER, inspectionType, hazardRating, new Date());
        } catch (Exception e){
            exception = e;
        } finally {
            assertNotNull(exception);
            assertTrue(exception instanceof IllegalArgumentException);
        }
    }
}
