import java.time.LocalDateTime;
import java.util.List;

/**
 * IPlnner Interface
 * 
 * @author Zheyi Chen
 *
 */
public interface IPlanner {

    /**
     * private field: schedule: Schedule
     * 
     * private field: routes: Map of routeID->routeName
     * 
     * private field: routeNameToID: Map
     * 
     * private field: Stations:Map of station id -> station objects
     * 
     * private field: stationNameToID: Map of station Name -> Station Id
     * 
     * private filed: currentTime: LocalDateTime: user input of the current time
     * 
     * private field: destinationStationName: String. Stores the user input of the
     * name of the destination station
     * 
     * private filed: departureStationName: String. Stores the user input of the
     * name of the departure station
     * 
     * private field: destinationStation: Station. Stores the user input of the
     * destination station
     * 
     * private filed: departureStationName: Station. Stores the user input of the
     * departure station
     */

    /**
     * Begin running the planner
     * 
     */
    void start();

    /**
     * Read the stops file and initialize the two HashMaps stations and
     * stationNameToID
     * 
     * @param stopsFile - filePath to stopsFile
     */
    void initStops(String stopsFile);

    /**
     * Read the routes file and initialize the two HashBiMaps routes and
     * routesNameToID
     * 
     * @param routesFile - filePath to routesFile
     */
    void initRoutes(String routesFile);

    /**
     * Initialize a map that maps a station name to a Station Object
     */
    void generateNameToStationIDMap();

    /**
     * calculate the estimated arrival time by walking between two Stations In the
     * method, we need to firstly calculate the distance between the starting
     * station and the destination station, then assume a constant walking speed, to
     * generate the time required for walking, then get the estimated arrival time
     * 
     * @param stationDeparture   - departure station
     * @param stationDestination - destination station
     * @param currTime           - the current time
     * 
     * @return a Time object represents the estimated arrival time by walking from
     *         the source station to the destination station
     */
    LocalDateTime ArrivalTimeByWalking(IStation stationDeparture, IStation stationDestination,
            LocalDateTime currTime);

    /**
     * Helper method to calculate the Euclidean distance between two stations
     * 
     * @param A - Station Object
     * @param B - Station Object
     * 
     * @return Distance (unit for distance is kilometer) between A and B
     */
    double Distance(IStation A, IStation B);

    /**
     * Helper method to converts decimal degrees to radians
     * 
     * @param degree - degree from 0 to 180
     * @return radian value of the degree
     */
    double degToRad(double degree);

    /**
     * Helper method to convert radians to decimal degrees
     * 
     * @param radian - radian value of the degree
     * @return degree corresponding to the passed in radian value
     */
    double radToDeg(double radian);

    /**
     * Given the departure station and the destination station calculate the trip(s)
     * that takes the least time to reach the destination from departure station
     * only considering the direct one trips
     * 
     * @param departure   departure station
     * @param destination destination station
     * @param currTime    the starting time
     * 
     * @return the earliest arrival time and tripID to take by taking one way trips
     */
    List<ITrip> GetOnewayTrips(IStation departure, IStation destination, LocalDateTime currTime);

    /**
     * Given the departure station and the destination station calculate the
     * earliest arrival time by considering potentially multiple trips
     * 
     * Should run BFS heuristically with stations within the range print the entire
     * itinerary for us
     * 
     * @param departure   - departure station
     * @param destination - destination station
     * @param currTime    - the starting time
     * 
     */
    void PrintMultiTrips(IStation departure, IStation destination, LocalDateTime currTime);

}
