import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Implementation of ITrip interface
 * 
 * @author Zheyi Chen
 *
 */
public class Trip implements ITrip {

    private int tripID;
    private String routeID;
    // the map stops stores stopId as key, arrive and dept time as value
    private HashMap<Integer, LocalDateTime[]> stops;

    /**
     * Constructor for Trip
     * 
     * @param tripID  - ID of a trip
     * @param routeID - routeID of a trip
     * 
     */
    public Trip(int tripID, String routeID) {
        this.tripID = tripID;
        this.routeID = routeID;
        this.stops = new HashMap<>();
    }

    @Override
    public int getTripId() {
        return this.tripID;
    }

    @Override
    public String getRouteId() {
        return this.routeID;
    }

    @Override
    public Collection<Integer> StopsOnTheTrip() {
        return this.stops.keySet();
    }

    @Override
    public boolean hasStop(int stopID) {
        return this.stops.containsKey(stopID);
    }

    @Override
    public LocalDateTime arriveAt(int stopID) {
        return stops.get(stopID)[0];
    }

    @Override
    public LocalDateTime departFrom(int stopID) {
        return stops.get(stopID)[1];
    }

    @Override
    public void addNewStop(int stopID, LocalDateTime arrive, LocalDateTime dept) {
        LocalDateTime[] times = new LocalDateTime[2];
        times[0] = arrive;
        times[1] = dept;
        this.stops.put(stopID, times);
    }

    @Override
    public List<Integer> laterThan(LocalDateTime currentTime) {
        ArrayList<Integer> validStops = new ArrayList<>();
        for (int stopID : stops.keySet()) {
            // if the arrive time of this trip to a certain stop is later than the current
            // time
            // then this stop could be a potential intermediate stop between source and
            // destination
            // add it to the validStops list
            if (stops.get(stopID)[0].isAfter(currentTime)) {
                validStops.add(stopID);
            }
        }
        return validStops;
    }

    @Override
    public boolean directToDestination(LocalDateTime currentTime, int source, int destination) {
        // check if can start from the source station and direct reach the
        // destination via taking this trip

        LocalDateTime startTime = null;
        LocalDateTime destTime = null;
        if (hasStop(source)) {
            startTime = departFrom(source);
        }
        if (hasStop(destination)) {
            destTime = arriveAt(destination);
        }

        if (startTime != null && destTime != null && startTime.isBefore(destTime)
                && startTime.isAfter(currentTime)) {
            return true;
        }

        return false;
    }

}
