/**
 * IStation Interface
 * 
 * @author Ziyi He
 *
 */
public interface IStation {
    /**
     * Private fields should include: String stationName, int stationID, double
     * longitude, double latitude, List<Integer> tripsGoThrough
     */
    /**
     * Station name getter method
     * 
     * @return the name of the Station
     */
    String getStationName();

    /**
     * Station ID getter method
     * 
     * @return the ID of the station
     */
    int getStationID();

    /**
     * Station location getter method
     * 
     * @return the Longitude of the station
     */
    double getLongitude();

    /**
     * Station location getter method
     * 
     * @return the Latitude of the station
     */
    double getLatitude();

    /**
     * Judge if a station supports wheel chair on boarding if so return true,
     * otherwise return false
     * 
     * @return boolean value suggests if the station supports wheel chair on
     *         boarding
     */
    boolean hasWheelChairAccess();

//    /**
//     * Create a sorted map from arrive time to trip id that go through
//     * currentStation (and store in private variables)
//     * 
//     * @param None
//     * 
//     * @return a map from time to trip id
//     */
//    TreeMap<Time, Integer> createSortedMap();

//    /**
//     * Find the trips that go through current station at a later time and can arrive at a given
//     * destination
//     * 
//     * @param departure time and destination station
//     * 
//     * @return a list of Integers
//     */
//
//    List<Integer> findNextTrip(Time departureTime, IStation destination);

}
