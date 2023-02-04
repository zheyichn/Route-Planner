import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

class ScheduleTest {

    Schedule s = new Schedule();
    String tripsFilePath = "./ScheduleTestFiles/trips.txt";
    String dayStopTimesFilePath = "./ScheduleTestFiles/stop_times_day.txt";
    String nightStopTimesFilePath = "./ScheduleTestFiles/stop_times_night.txt";

    @Test
    void testLoadFiles() {
        s.loadTrips(tripsFilePath);
        s.loadStopTimes(dayStopTimesFilePath);

        ITrip trip = s.getTrip(000002);

        // check the trip id is correctly saved correctly in the trip object
        assertEquals(000002, trip.getTripId());
        // check the route id is correctly saved in the trip object
        assertEquals("2", trip.getRouteId());
        // check the stops information is saved correctly in the trip object
        Set<Integer> expectedStopIDs = new HashSet<>();
        expectedStopIDs.add(1);
        expectedStopIDs.add(7);
        expectedStopIDs.add(3);
        expectedStopIDs.add(8);
        expectedStopIDs.add(10);
        Set<Integer> actualStopIDs = (Set<Integer>) trip.StopsOnTheTrip();
        for (Integer stopId : expectedStopIDs) {
            assertTrue(actualStopIDs.contains(stopId));
        }
    }

    @Test
    void testGetStopIDToTripIDMap() {
        s.loadTrips(tripsFilePath);
        s.loadStopTimes(dayStopTimesFilePath);

        Map<Integer, Map<LocalDateTime, List<Integer>>> map = s.getStopIDToTripIDMap();
        Map<LocalDateTime, List<Integer>> actualNumTrips = map.get(5);
        assertEquals(2, actualNumTrips.size());

        List<Integer> acutalTripID = map.get(5).get(LocalDateTime.parse("0000-01-01T07:15:00"));
        assertEquals(1, acutalTripID.size());
        assertEquals(4, acutalTripID.get(0));
    }

    @Test
    void testFindEarliestTripDay() {
        s.loadTrips(tripsFilePath);
        s.loadStopTimes(dayStopTimesFilePath);

        List<ITrip> trips = s.findEarliestTrip(1, 3, LocalDateTime.parse("0000-01-01T07:00:00"));
        assertEquals(1, trips.size());
        assertEquals(3, trips.get(0).getTripId());
    }

    @Test
    void testFindEarliestTripNight() {
        s.loadTrips(tripsFilePath);
        s.loadStopTimes(nightStopTimesFilePath);

        List<ITrip> trips = s.findEarliestTrip(8, 10, LocalDateTime.parse("0000-01-01T23:00:00"));
        assertEquals(1, trips.size());
        assertEquals(3, trips.get(0).getTripId());
    }

    @Test
    void testFindStationAndArrivalTime() {
        s.loadTrips(tripsFilePath);
        s.loadStopTimes(dayStopTimesFilePath);

        List<SimpleEntry<Integer, LocalDateTime>> actual = s.findStationAndArrivalTime(2,
                LocalDateTime.parse("0000-01-01T07:05:00"),
                LocalDateTime.parse("0000-01-01T07:20:00"));

        assertEquals(3, actual.size());
    }
}
