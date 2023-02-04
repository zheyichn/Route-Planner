import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StationTest {

    public static double DELTA = 0.001;

    @Test
    public void getterTest() {
        Station station1 = new Station(0, "Walnut St & 33 St", 39.951759, -75.18345, 0);
        Station station2 = new Station(1, "54th-City", 39.11113, -75.0125, 1);
        assertEquals(0, station1.getStationID());
        assertEquals(1, station2.getStationID());
        assertEquals("Walnut St & 33 St", station1.getStationName());
        assertEquals("54th-City", station2.getStationName());
        assertEquals(station1.getLatitude(), 39.951, DELTA);
        assertEquals(station1.getLongitude(), -75.183, DELTA);
        assertEquals(station2.getLatitude(), 39.111, DELTA);
        assertEquals(station2.getLongitude(), -75.012, DELTA);
    }

    @Test
    public void wheelChairAccess() {
        Station station1 = new Station(0, "Walnut St & 33 St", 39.951759, -75.18345, 0);
        Station station2 = new Station(1, "54th-City", 39.11113, -75.0125, 1);
        Station station3 = new Station(0, "Graterford", 39.93902, -74.342789, 2);
        assertFalse(station1.hasWheelChairAccess());
        assertTrue(station2.hasWheelChairAccess());
        assertFalse(station3.hasWheelChairAccess());
    }

}
