import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

public class TripTest {

    @Test
    public void getterTest() {
        Trip trip = new Trip(829863, "M896");
        assertEquals(829863, trip.getTripId());
        assertEquals("M896", trip.getRouteId());
        assertTrue(trip.StopsOnTheTrip().isEmpty());
    }

    @Test
    public void searchStopsTimeTest() {
        Trip trip = new Trip(829863, "M896");

        LocalDateTime timeArrive1 = LocalDateTime.parse("0000-01-01T20:06:00");
        LocalDateTime timeDep1 = LocalDateTime.parse("0000-01-01T20:06:00");

        LocalDateTime timeArrive2 = LocalDateTime.parse("0000-01-01T20:17:00");
        LocalDateTime timeDep2 = LocalDateTime.parse("0000-01-01T20:17:00");

        LocalDateTime timeArrive3 = LocalDateTime.parse("0000-01-01T08:20:00");
        LocalDateTime timeDep3 = LocalDateTime.parse("0000-01-01T08:20:00");

        LocalDateTime timeArrive4 = LocalDateTime.parse("0000-01-01T16:40:00");
        LocalDateTime timeDep4 = LocalDateTime.parse("0000-01-01T16:40:00");

        trip.addNewStop(1, timeArrive1, timeDep1);
        trip.addNewStop(2, timeArrive2, timeDep2);
        trip.addNewStop(3, timeArrive3, timeDep3);
        trip.addNewStop(4, timeArrive4, timeDep4);

        assertTrue(trip.hasStop(1));
        assertTrue(trip.hasStop(2));
        assertTrue(trip.hasStop(3));
        assertTrue(trip.hasStop(4));

        assertFalse(trip.hasStop(0));
        assertFalse(trip.hasStop(12));

        LocalDateTime testTime1 = LocalDateTime.parse("0000-01-01T20:06:00");
        LocalDateTime testTime2 = LocalDateTime.parse("0000-01-01T20:06:00");
        assertEquals(testTime1, trip.arriveAt(1));
        assertEquals(testTime2, trip.departFrom(1));
    }

    @Test
    public void LaterThanTest() {

        Trip trip = new Trip(829863, "M896");

        LocalDateTime timeArrive1 = LocalDateTime.parse("0000-01-01T20:06:00");
        LocalDateTime timeDep1 = LocalDateTime.parse("0000-01-01T20:06:00");

        LocalDateTime timeArrive2 = LocalDateTime.parse("0000-01-01T20:17:00");
        LocalDateTime timeDep2 = LocalDateTime.parse("0000-01-01T20:17:00");

        LocalDateTime timeArrive3 = LocalDateTime.parse("0000-01-01T08:20:00");
        LocalDateTime timeDep3 = LocalDateTime.parse("0000-01-01T08:20:00");

        LocalDateTime timeArrive4 = LocalDateTime.parse("0000-01-01T16:40:00");
        LocalDateTime timeDep4 = LocalDateTime.parse("0000-01-01T16:40:00");

        trip.addNewStop(1, timeArrive1, timeDep1);
        trip.addNewStop(2, timeArrive2, timeDep2);
        trip.addNewStop(3, timeArrive3, timeDep3);
        trip.addNewStop(4, timeArrive4, timeDep4);

        LocalDateTime currTime = LocalDateTime.parse("0000-01-01T18:15:00");
        assertEquals(2, trip.laterThan(currTime).size());
        assertTrue(trip.laterThan(currTime).contains(1));
        assertTrue(trip.laterThan(currTime).contains(2));

    }

    @Test
    public void directToDestinationTest() {
        Trip trip = new Trip(829863, "M896");

        LocalDateTime timeArrive1 = LocalDateTime.parse("0000-01-01T20:06:00");
        LocalDateTime timeDep1 = LocalDateTime.parse("0000-01-01T20:06:00");

        LocalDateTime timeArrive2 = LocalDateTime.parse("0000-01-01T20:17:00");
        LocalDateTime timeDep2 = LocalDateTime.parse("0000-01-01T20:17:00");

        LocalDateTime timeArrive3 = LocalDateTime.parse("0000-01-01T08:20:00");
        LocalDateTime timeDep3 = LocalDateTime.parse("0000-01-01T08:20:00");

        LocalDateTime timeArrive4 = LocalDateTime.parse("0000-01-01T16:40:00");
        LocalDateTime timeDep4 = LocalDateTime.parse("0000-01-01T16:40:00");

        trip.addNewStop(1, timeArrive1, timeDep1);
        trip.addNewStop(2, timeArrive2, timeDep2);
        trip.addNewStop(3, timeArrive3, timeDep3);
        trip.addNewStop(4, timeArrive4, timeDep4);

        LocalDateTime currTime = LocalDateTime.parse("0000-01-01T18:15:00");
        assertTrue(trip.directToDestination(currTime, 1, 2));
        assertFalse(trip.directToDestination(currTime, 3, 2));

        LocalDateTime timeArrive5 = LocalDateTime.parse("0000-01-01T21:06:00");
        LocalDateTime timeDep5 = LocalDateTime.parse("0000-01-01T21:06:00");

        LocalDateTime timeArrive6 = LocalDateTime.parse("0000-01-01T22:43:00");
        LocalDateTime timeDep6 = LocalDateTime.parse("0000-01-01T22:43:00");

        LocalDateTime timeArrive7 = LocalDateTime.parse("0000-01-01T23:20:00");
        LocalDateTime timeDep7 = LocalDateTime.parse("0000-01-01T23:20:00");

        LocalDateTime timeArrive8 = LocalDateTime.parse("0000-01-02T00:40:00");
        LocalDateTime timeDep8 = LocalDateTime.parse("0000-01-02T00:40:00");

        trip.addNewStop(5, timeArrive5, timeDep5);
        trip.addNewStop(6, timeArrive6, timeDep6);
        trip.addNewStop(7, timeArrive7, timeDep7);
        trip.addNewStop(8, timeArrive8, timeDep8);

        LocalDateTime currTime2 = LocalDateTime.parse("0000-01-01T20:13:00");
        assertTrue(trip.directToDestination(currTime2, 5, 7));
        assertTrue(trip.directToDestination(currTime2, 5, 8));
        assertFalse(trip.directToDestination(currTime2, 2, 1));
        assertTrue(trip.directToDestination(currTime2, 7, 8));
        assertTrue(currTime2.isBefore(timeArrive8));
        assertFalse(trip.directToDestination(currTime2, 8, 7));
    }
}
