package com.example.cmpt276project;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cmpt276project.model.CsvDataParser;
import com.example.cmpt276project.model.DateHelper;
import com.example.cmpt276project.model.Inspection;
import com.example.cmpt276project.model.Restaurant;
import com.example.cmpt276project.model.RestaurantManager;
import com.example.cmpt276project.model.Violation;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CsvDataParserTest {

    @Test
    public void shouldBeAbleToGetRestaurantFromData() {
        String restaurantData = "\"SDFO-8HKP7E\",\"Pattullo A&W\",\"12808 King George Blvd\",\"Surrey\",\"Restaurant\",49.20610961,-122.8668064";
        Restaurant sut = CsvDataParser.getRestaurantFromData(restaurantData);
        assertEquals("SDFO-8HKP7E", sut.getTrackingNumber());
        assertEquals("Pattullo A&W", sut.getName());
        assertEquals("12808 King George Blvd", sut.getAddress());
        assertEquals("Surrey", sut.getCity());
        assertTrue(Math.abs(sut.getLatitude() - 49.20610961) < 0.000001);
        assertTrue(Math.abs(sut.getLongitude() - (-1 * 122.8668064)) < 0.000001);
    }

    @Test
    public void shouldBeAbleToGetInspectionFromData() {
        String inspectionData = "\"SDFO-8HKP7E\",20181024,\"Follow-Up\",0,1,\"Low\",\"308,Not Critical,Equipment/utensils/food contact surfaces are not in good working order [s. 16(b)],Not Repeat\"";
        GregorianCalendar expectedDate = DateHelper.getDateFromString("20181024");
        Inspection sut = CsvDataParser.getInspectionFromData(inspectionData);
        assertEquals("SDFO-8HKP7E", sut.getTrackingNumber());
        assertEquals(expectedDate, sut.getDate());
        assertEquals(Inspection.InspectionType.FOLLOW_UP, sut.getInspectionType());
        assertEquals(Inspection.HazardRating.LOW, sut.getHazardRating());
    }

    @Test
    public void shouldBeAbleToGetViolationFromData() {
        String violationData = "308,Not Critical,Equipment/utensils/food contact surfaces are not in good working order [s. 16(b)],Not Repeat";
        Violation sut = CsvDataParser.getViolationFromData(violationData);
        assertEquals(308, sut.getId());
        assertFalse(sut.isCritical());
        assertFalse(sut.isRepeat());
        assertEquals("Equipment/utensils/food contact surfaces are not in good working order [s. 16(b)]", sut.getDescription());
    }

    @Test
    public void shouldBeAbleToInitializeRestaurantManagerFromData() {
        RestaurantManager sut = RestaurantManager.getInstance();
        int numInspections = 0;
        for (Restaurant restaurant : sut) {
            for (Inspection inspection : restaurant) {
                numInspections++;
            }
        }
        assertEquals(8, sut.getLength());
        assertEquals(55, numInspections);
    }

}