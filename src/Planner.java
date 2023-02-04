import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Implementation of interface IPlanner
 * 
 * @author Justin Ng, Ziyi He, Zheyi Chen, Liujia Wang
 *
 */
public class Planner implements IPlanner {

    // a map of all station id to all station objects
    private HashMap<Integer, Station> stations;
    private HashMap<String, Integer> stationNameToID;
    private HashMap<String, String> routes;// routeID->routeName
    private HashMap<String, String> routesNameToID;
    private ISchedule busSchedule;
    private String depStationName;
    private String destStationName;
    private Station depStation;
    private Station destStation;
    private int depStationID;
    private int destStationID;
    private LocalDateTime depTime;
    private LocalDateTime arrivalTimeByWalking;
    private LocalDateTime arrivalTimeByOnewayTrips;
    private int userDepth = 5;

    /**
     * A Planner Constructor Initialize fields to empty maps
     */
    public Planner() {
        this.busSchedule = new Schedule();
        this.stations = new HashMap<>();
        this.stationNameToID = new HashMap<>();
        this.routes = new HashMap<>();
        this.routesNameToID = new HashMap<>();
    }

    @Override
    public void start() {
        // initialize a scanner
        Scanner scnr = new Scanner(System.in);

        // 1. stops
        System.out.println("Reading stops information...");
        initStops("./resources/stops.txt");
        System.out.println("Finished reading stops information...\n");

        // 2. routes
        System.out.println("Reading routes information...");
        initRoutes("./resources/routes.txt");
        System.out.println("Finished reading routes information...\n");

        // 3. trips and stop times
        System.out.println("Reading trip information...");
        busSchedule.loadTrips("./resources/trips.txt");
        System.out.println("Finished reading trip information...\n");

        // 4. stop times
        System.out.println("Reading stop time information...");
        busSchedule.loadStopTimes("./resources/stop_times.txt");
        System.out.println("Finished reading stop time information...\n");

        // 5. Keep taking in query for user
        while (true) {
            // current time
            System.out
                    .println("Please specify your target departure time in the format of HH:MM:SS");
            String depTimeStr = scnr.nextLine();
            // check for validity first
            String[] depTimeContents = depTimeStr.split(":");
            if (depTimeContents.length != 3) {
                System.out.println("Correct usage: HH:MM:SS (e.g. 18:00:00)");
                continue;
            }

            boolean timeIsValid = true;
            for (String s : depTimeContents) {
                if (s.length() != 2) {
                    timeIsValid = false;
                    break;
                }
            }
            if (!timeIsValid) {
                System.out.println("Correct usage: HH:MM:SS (e.g. 18:00:00)");
                continue;
            }

            if (Integer.parseInt(depTimeContents[0]) < 0
                    || Integer.parseInt(depTimeContents[0]) > 23
                    || Integer.parseInt(depTimeContents[1]) < 0
                    || Integer.parseInt(depTimeContents[1]) > 59
                    || Integer.parseInt(depTimeContents[2]) < 0
                    || Integer.parseInt(depTimeContents[2]) > 59) {
                System.out.println("Time provided out of range.");
                continue;
            }
            ;

            // Convert the string object to LocalDateTime
            depTime = LocalDateTime.parse("0000-01-01T" + depTimeStr);

            while (true) {
                System.out.println("Where are you departing from?");
                depStationName = scnr.nextLine();
                if (stationNameToID.get(depStationName) != null) {
                    depStationID = stationNameToID.get(depStationName);
                    depStation = stations.get(depStationID);
                    break;
                }
                System.out.println("No such station exists. Please re-enter.");
            }

            while (true) {
                System.out.println("Where is your destination?");
                destStationName = scnr.nextLine();
                if (stationNameToID.get(destStationName) != null) {
                    destStationID = stationNameToID.get(destStationName);
                    destStation = stations.get(destStationID);
                    break;
                }
                System.out.println("No such station exists. Please re-enter.");
            }

            System.out.println("Max number of transfers? Type 99 if it doesn't matter.");
            userDepth = scnr.nextInt();
            scnr.nextLine(); // clean up the "\n character"

            // Initialize walking time
            // RUN modified dijkstra's and print the itinerary
            arrivalTimeByWalking = ArrivalTimeByWalking(depStation, destStation, depTime);
            List<ITrip> listOfOnewayTrips = GetOnewayTrips(depStation, destStation, depTime);
            if (listOfOnewayTrips.isEmpty()) {
                arrivalTimeByOnewayTrips = arrivalTimeByWalking.plusMinutes(1);
            } else {
                arrivalTimeByOnewayTrips = listOfOnewayTrips.get(0).arriveAt(destStationID);
            }
            PrintMultiTrips(depStation, destStation, arrivalTimeByOnewayTrips);

            // Ask the user if it wants to find the best route for another itinerary
            boolean repeatIsNeeded = true;
            boolean responseIsValid = false;
            while (!responseIsValid) {
                System.out.println(
                        "Do you want to find the best route for another itinerary? \"Y\" or \"N\"");
                String repeat = scnr.nextLine();
                if (repeat.equals("Y")) {
                    responseIsValid = true;
                } else if (repeat.equals("N")) {
                    responseIsValid = true;
                    repeatIsNeeded = false;
                }
            }
            if (!repeatIsNeeded) {
                break;
            }

        }

        scnr.close();

    }

    @Override
    public void initStops(String stopsFile) {
        // initialize the Stations field from stops.txt
        try {
            BufferedReader br = new BufferedReader(new FileReader(stopsFile));
            String line = br.readLine(); // skip the header line
            line = br.readLine();
            while (line != null) {
                String[] lineContents = line.split(",");

                int stationID = Integer.parseInt(lineContents[0]);
                String stationName = lineContents[1];
                double stationLat = Double.parseDouble(lineContents[2]);
                double stationLon = Double.parseDouble(lineContents[3]);
                int wheelchiar = Integer.parseInt(lineContents[7]);
                Station newStation = new Station(stationID, stationName, stationLat, stationLon,
                        wheelchiar);
                stations.put(stationID, newStation);

                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // generate a reverse map by calling helper method
        // after stations is created
        generateNameToStationIDMap();
    }

    @Override
    public void initRoutes(String routesFile) {

        // initialize the Routes field from routes.txt
        try {
            BufferedReader br = new BufferedReader(new FileReader(routesFile));
            String line = br.readLine(); // skip the header line
            line = br.readLine();
            while (line != null) {
                String[] lineContents = line.split(",");

                String routeID = lineContents[0];
                String routeName = lineContents[2];
                this.routes.put(routeID, routeName);

                line = br.readLine();
            }
            // create a reverse map of routeName to routeID
            for (String key : this.routes.keySet()) {
                this.routesNameToID.put(this.routes.get(key), key);
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void generateNameToStationIDMap() {
        for (int stationID : this.stations.keySet()) {
            this.stationNameToID.put(this.stations.get(stationID).getStationName(), stationID);
        }
    }

    @Override
    public LocalDateTime ArrivalTimeByWalking(IStation stationDeparture,
            IStation stationDestination, LocalDateTime depTime) {
        double distanceInKm = Distance(stationDeparture, stationDestination);
        // assume by walking the speed is 80 meters per minute
        long minsToWalk = (long) ((distanceInKm * 1000.0) / 80);
        // passed in currTime is immutable, return a copy of currTime added by the
        // minsToWalk
        LocalDateTime ret = depTime.plusMinutes(minsToWalk);
//        this.arrivalTimeByWalking = ret;
        return ret;
    }

    @Override
    public double Distance(IStation A, IStation B) {
        double lat1 = ((Station) A).getLatitude();
        double lon1 = ((Station) A).getLongitude();
        double lat2 = ((Station) B).getLatitude();
        double lon2 = ((Station) B).getLongitude();

        double theta = lon1 - lon2;
        double dist = Math.sin(degToRad(lat1)) * Math.sin(degToRad(lat2))
                + Math.cos(degToRad(lat1)) * Math.cos(degToRad(lat2)) * Math.cos(degToRad(theta));
        dist = Math.acos(dist);
        dist = radToDeg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return dist;
    }

    @Override
    public double degToRad(double degree) {
        return (degree * Math.PI / 180.0);
    }

    @Override
    public double radToDeg(double radian) {
        return (radian * 180.0 / Math.PI);
    }

    @Override
    public List<ITrip> GetOnewayTrips(IStation departure, IStation destination,
            LocalDateTime depTime) {
        int sourceID = departure.getStationID();
        int destID = destination.getStationID();
        return ((Schedule) busSchedule).findEarliestTrip(sourceID, destID, depTime);
    }

    @Override
    public void PrintMultiTrips(IStation departure, IStation destination, LocalDateTime currTime) {
        LocalDateTime baseTime = arrivalTimeByWalking.isBefore(arrivalTimeByOnewayTrips)
                ? arrivalTimeByWalking
                : arrivalTimeByOnewayTrips;
//        System.out.println(baseTime);
        StationWrapper earliestStationWrapper = modifiedDijkstra(departure, destination, baseTime,
                userDepth);
//        System.out.printf("Earliest Station Wrapper: %s\n",
//                stations.get(earliestStationWrapper.stationID).getStationName());
        if (earliestStationWrapper == null) {
            System.out.printf("It is faster to walk, your expected arrival time is %02d:%02d\n",
                    baseTime.getHour(), baseTime.getMinute());
        } else {
            printAllStationWrappers(earliestStationWrapper);
        }
    }

    // a wrapper class over IStation to include arrivalTime and predecessor station
    // used in modifiedDijkstra
    /**
     * A helper class to backtrack the predecessors
     * 
     * @author Ziyi He
     *
     */
    class StationWrapper {
        /**
         * public field stationID
         */
        int stationID;
        /**
         * public field arriveTime
         */
        LocalDateTime arrivalTime;
        /**
         * public field tracks the predecessor station
         */
        StationWrapper predStation;
        /**
         * public field suggests the number of transfers we have made
         */
        int depth;
        /**
         * public field tripID of the trip that this station is on
         */
        int tripID;

        /**
         * Constructor for StationWrapper
         * 
         * @param s - stationID
         * @param a - LocalDateTime
         * @param p - a stationWrapper object
         * @param d - depth so far
         * @param t - tripID
         */
        StationWrapper(int s, LocalDateTime a, StationWrapper p, int d, int t) {
            stationID = s;
            arrivalTime = a;
            predStation = p; // 0 for null
            depth = d;
            tripID = t;
        }
    }

    /**
     * New comparator to compare the station Wrapper objects
     * 
     * @author Ziyi He
     *
     */
    class StationWrapperComparator implements Comparator<StationWrapper> {
        // Overriding compare() method of Comparator for order of arrivalTime
        public int compare(StationWrapper s1, StationWrapper s2) {
            if (s1.arrivalTime.isBefore(s2.arrivalTime)) {
                return -1;
            } else if (s1.arrivalTime.isAfter(s2.arrivalTime)) {
                return 1;
            } else {
                if (s1.depth < s2.depth) {
                    return -1;
                } else if (s1.depth > s2.depth) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    /**
     * Run modifiedDijkstra's algo to find the route that is most time-saving
     * 
     * @param departure   - departure station object
     * @param destination - destination station object
     * @param baseTime    - current time from user input
     * @param userDepth   - the max number of bus transfer the user input
     * @return the destination StationWrapper
     */

    private StationWrapper modifiedDijkstra(IStation departure, IStation destination,
            LocalDateTime baseTime, int userDepth) {

        // get the starting and destination station IDs
        int depID = departure.getStationID();
        int destID = destination.getStationID();

        // initialize a minheap
        PriorityQueue<StationWrapper> minHeap = new PriorityQueue<>(10,
                new StationWrapperComparator());

        // insert departure station into minHeap
        // station wrappers carry (station_id, arrival time at station, pred, depth,
        // trip_id)
        StationWrapper depWrap = new StationWrapper(depID, depTime, null, 0, 0);
        minHeap.add(depWrap);

        // initialize a hashMap stationVisited (station id -> StationWrapper Object)
        // and a hashSet tripVisited
        HashMap<Integer, StationWrapper> stationVisited = new HashMap<>();
        HashSet<Integer> tripExplored = new HashSet<>();
        // modifiedDijkstra
        while (!minHeap.isEmpty()) {
            StationWrapper u = minHeap.poll();
            // check if exceeds the maximum number of transfers (depth > userDepth)
            if (u.depth > userDepth + 1) {
                continue;
            }
            Integer uID = u.stationID;
            // sanity checks
            // check if we have already found the destination
            if (uID == destID) {
                return u;
            }
            // check if the station is already "popped". If so, continue
            if (stationVisited.containsKey(uID) && u.depth >= stationVisited.get(uID).depth) {
                continue;
            }

            // after passing the sanity checks
            // get a list of trips going through u after we arrive u and before base time
            if (u.arrivalTime.isAfter(baseTime)) {
                continue;
            }

            Collection<List<Integer>> collectionOfTrips = ((TreeMap<LocalDateTime, List<Integer>>) busSchedule
                    .getStopIDToTripIDMap().get(uID)).subMap(u.arrivalTime, baseTime).values();

            // for each of these trips
            for (List<Integer> trips : collectionOfTrips) {
                for (Integer tripID : trips) {
                    // otherwise, add it to the list of trips considered
                    // get a list of stops on trip, with arrival times later than our current time
                    // but before our base time
                    tripExplored.add(tripID);
                    List<SimpleEntry<Integer, LocalDateTime>> midStops = busSchedule
                            .findStationAndArrivalTime(tripID,
                                    busSchedule.getTrip(tripID).arriveAt(uID), baseTime);

                    // for each of these stops
                    for (SimpleEntry<Integer, LocalDateTime> midStop : midStops) {
                        // if the stop has already been "popped" once from Q
                        // that means there's no way its arrival time can further decrease, hence
                        // continue
                        if (stationVisited.containsKey(midStop.getKey())) {
                            continue;
                        }
                        // if it is not "popped" or not in Q
                        StationWrapper newSW = new StationWrapper(midStop.getKey(),
                                midStop.getValue(), u, u.depth + 1, tripID);
                        minHeap.add(newSW);

                        // if we have reached destination
                        // update baseTime if our current arrivaltime is earlier
                        if (newSW.stationID == destID && newSW.depth <= userDepth) {
                            baseTime = baseTime.isBefore(newSW.arrivalTime) ? baseTime
                                    : newSW.arrivalTime;
                        }
                    }
                }
                // after visiting all the stops we can go from a particular stop i
                // add the stop i to our stationVisited
                stationVisited.put(uID, u);
            }

        }
        return null; // cannot found. Never will happen, because we have walking option.

    }

    /**
     * To backtrack and print out the route given a stationWrapper
     * 
     * @param sw - the last stationWrapper
     */
    private void printAllStationWrappers(StationWrapper sw) {

        // First, get a list of station wrappers in the order of "pred station" until it
        // is source
        LinkedList<StationWrapper> stationsOnRoute = new LinkedList<StationWrapper>();
        while (true) {
            stationsOnRoute.addFirst(sw);
            if (sw.predStation != null) {
                sw = sw.predStation;
                continue;
            }
            break;
        }

        int counter = 1;
        for (StationWrapper testSW : stationsOnRoute) {
            counter++;
        }

        // For each station, we get the attributes we want to print
        // i) stationID -> current station name
        // ii) tripID of next station (if there is one) -> route number
        // iii) tripID of next station (if there is one) -> dept time from current
        // station
        // iv) arrivalTime to next station (if there is one)
        // v) stationID of next station (if there is one)
        // vi) depth (i.e. number of trips we are currently doing)
        int numOfSW = stationsOnRoute.size();
        StationWrapper currSW, nextSW;
        int currSID, nextSID, currDepth;
        ITrip currTrip;
        LocalDateTime depTimeFromCurrSW, arrivalTimeAtNextSW;

        System.out.println("--- Start of itinerary ---");
        while (numOfSW > 0) {
            currSW = stationsOnRoute.pop();
            currSID = currSW.stationID;
            currDepth = currSW.depth;
            // if there is still next station
            if (numOfSW > 1) {
                nextSW = stationsOnRoute.peek();
                nextSID = nextSW.stationID;
                currTrip = busSchedule.getTrip(nextSW.tripID);
                depTimeFromCurrSW = currTrip.departFrom(currSID);
                arrivalTimeAtNextSW = currTrip.arriveAt(nextSID);
                System.out.printf("Trip number %d: Take Bus Route %s from %s to %s\n", currDepth,
                        currTrip.getRouteId(), stations.get(currSID).getStationName(),
                        stations.get(nextSID).getStationName());
                System.out.printf(
                        "    Expected departure: %02d:%02d, Expected arrival: %02d:%02d\n",
                        depTimeFromCurrSW.getHour(), depTimeFromCurrSW.getMinute(),
                        arrivalTimeAtNextSW.getHour(), arrivalTimeAtNextSW.getMinute());
            } else {
                System.out.println("--- End of itinerary ---");
            }
            numOfSW--;
        }

    }

}
