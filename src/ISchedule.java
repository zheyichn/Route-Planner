import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Interface Ischedule
 * 
 * @author Ziyi He
 *
 */
public interface ISchedule {
    /**
     * This is a class serves as a "trip family" that stores trip objects
     * 
     * private field: Map<Integer, Trip> tripIDToTrip
     */
    /**
     * Load trips from .txt files - "trip.txt"
     * 
     * @param tripFilePath - the path of the txt data
     * 
     * @return a map consisting of Trip objects. The key is tripID, the value is the
     *         corresponding trip objects
     */
    Map<Integer, ITrip> loadTrips(String tripFilePath);

    /**
     * Load stop times from .txt files - "stop_times.txt" and save the stops
     * information into Trip object
     * 
     * @param stopTimesFilePath - the file path of the stop_times.txt
     * @return a map of tripID to Trip Objects
     * 
     */
    Map<Integer, ITrip> loadStopTimes(String stopTimesFilePath);

    /**
     * Find the trip(s) that can arrive at destination at earliest time given a
     * source and current time
     * 
     * @param sourceStopID      - stop ID of the source station
     * 
     * @param destinationStopID - stop ID of the destination station
     * 
     * @param currentTime       - current time
     * 
     * @return the trip(s) that passes the source and arrives at destination the
     *         earliest
     */
    Collection<ITrip> findEarliestTrip(Integer sourceStopID, Integer destinationStopID,
            LocalDateTime currentTime);

    /**
     * Given a trip ID, return the corresponding trip object
     * 
     * @param tripID - a trip ID
     * 
     * @return a Trip object of specified TripID
     */
    ITrip getTrip(int tripID);

    /**
     * Get a map from stopID to map of time to trip ID list
     *
     * 
     * @return stopIDToTripIDSortedByTime map
     */
    Map<Integer, Map<LocalDateTime, List<Integer>>> getStopIDToTripIDMap();

    /**
     * Given trip ID and time range from begin to end, find the station ID and
     * arrival time pair
     * 
     * @param tripID - ID of a trip
     * @param begin  - a LocalDateTime object stands for the starting time
     * @param end    - a LocalDateTime object stands for the ending time
     * @return list of station ID and arrivalTime entries
     */
    List<SimpleEntry<Integer, LocalDateTime>> findStationAndArrivalTime(int tripID,
            LocalDateTime begin, LocalDateTime end);

}
