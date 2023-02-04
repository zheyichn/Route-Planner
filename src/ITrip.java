import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * ITrip Interface
 * 
 * @author Zheyi Chen
 */
public interface ITrip {
    /**
     * private fields of a Trip instance should include: tripID, routeID A, a
     * HashMap named stops contains a mapping of stopID to [arrivalTime,
     * departureTime] on this trip, or in other words, associated with above tripID.
     * 
     * HashMap Stops- e.g, stop_id -> [arriveTime1, departureTime1] Arrive and
     * departure time are LocalDateTime Object
     */
    /**
     * A getter method to get the tripID of this Trip Object
     * 
     * @return tripID of the Trip Object on which this method is called
     */
    int getTripId();

    /**
     * A getter method to get the routeID of this Trip Object A routeID is usually
     * associated with multiple TripIDs
     * 
     * @return routeID of this ITrip
     */
    String getRouteId();

    /**
     * Get the stops along this trip, return their IDs in a collection
     * 
     * @return a collection of IDs of stops along this trip
     */
    Collection<Integer> StopsOnTheTrip();

    /**
     * Check if the passed in stopID is contained along this trip
     * 
     * @param stopID - ID of a stop
     * 
     * @return boolean value suggests if the stop is contained this trip
     */
    boolean hasStop(int stopID);

    /**
     * Helper method to filer out stops whose arrive time is later the currentTime
     * parameter along this trip
     * 
     * @param currentTime - LocalDateTime object stands for the current time
     * @return a list of stopID whose arrive time is later than the currentTime
     */
    List<Integer> laterThan(LocalDateTime currentTime);

    /**
     * Helper method to make a judgment if there this trip can reach the destination
     * directly
     * 
     * @param currentTime - a LocalDateTime stands for the current Time
     * @param source      - departure station's ID
     * @param destination - destination station's ID
     * @return boolean value, true if directly accessible by this trip, otherwise
     *         return false
     */
    boolean directToDestination(LocalDateTime currentTime, int source, int destination);

    /**
     * Get the time arrives at a station in a trip. Remember to use hasStop() to
     * check if the stopID exists in stops before trying to get time information
     * 
     * @param stopID - ID of a stop
     * 
     * @return the time arrives at a certain station
     */
    LocalDateTime arriveAt(int stopID);

    /**
     * Get the time departure from a station in a trip. Remember to use hasStop() to
     * check if the stopID exists in stops before trying to get time information
     * 
     * @param stopID - ID of a stop
     * 
     * @return the time departure from a certain station
     */
    LocalDateTime departFrom(int stopID);

    /**
     * Add a new stop into the HashMap stops of a trip object Check if the stopId
     * already exists in stops before call this method
     * 
     * @param stopID - ID of the station will be added
     * @param arrive - LocalDateTimeObject stands for when this trip arrives this
     *               stop
     * @param dept   - LocalDateTime Object stands for when this trip departures
     *               from this trip
     * 
     */
    void addNewStop(int stopID, LocalDateTime arrive, LocalDateTime dept);

}
