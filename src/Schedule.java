import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * Implementation of ISchedule
 * 
 * @author Liujia Wang
 *
 */
public class Schedule implements ISchedule {

    private Map<Integer, ITrip> tripIDToTrip;
    private Map<Integer, Map<LocalDateTime, List<Integer>>> stopIDToTripIDSortedByTime;

    /**
     * A Constructor for schedule class
     */
    public Schedule() {
        tripIDToTrip = new HashMap<>();
        stopIDToTripIDSortedByTime = new HashMap<>();
    }

    @Override
    public Map<Integer, ITrip> loadTrips(String tripFilePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(tripFilePath));
            String line = br.readLine(); // skip the header line
            line = br.readLine();
            while (line != null) {
                String[] lineContents = line.split(",");
                String routeID = lineContents[0];
                int tripID = Integer.parseInt(lineContents[2]);
                // create new Trip object and add to tripIDToTrip map
                tripIDToTrip.put(tripID, new Trip(tripID, routeID));
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tripIDToTrip;
    }

    @Override
    public Map<Integer, ITrip> loadStopTimes(String stopTimesFilePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(stopTimesFilePath));
            String line = br.readLine(); // skip the header line
            line = br.readLine();

            while (line != null) {
                String[] lineContents = line.split(",");

                int tripID = Integer.parseInt(lineContents[0]);

                LocalDateTime arrivalTime;
                String[] arrialTimeContents = lineContents[1].split(":");
                // if the hour parameter is above 23, we reformat it by subtracting from 24 and
                // count it as next day
                if (Integer.parseInt(arrialTimeContents[0]) > 23) {
                    String temp = "0000-01-02T" + Integer.toString(0)
                            + Integer.toString(Integer.parseInt(arrialTimeContents[0]) - 24) + ":"
                            + arrialTimeContents[1] + ":" + arrialTimeContents[2];
                    arrivalTime = LocalDateTime.parse(temp);
                } else {
                    arrivalTime = LocalDateTime.parse("0000-01-01T" + lineContents[1]);
                }

                LocalDateTime departureTime;
                String[] departureTimeContents = lineContents[1].split(":");
                if (Integer.parseInt(departureTimeContents[0]) > 23) {
                    String temp = "0000-01-02T" + Integer.toString(0)
                            + Integer.toString(Integer.parseInt(departureTimeContents[0]) - 24)
                            + ":" + departureTimeContents[1] + ":" + departureTimeContents[2];
                    departureTime = LocalDateTime.parse(temp);
                } else {
                    departureTime = LocalDateTime.parse("0000-01-01T" + lineContents[1]);
                }

                int stopID = Integer.parseInt(lineContents[3]);

                if (tripIDToTrip.containsKey(tripID)) {
                    Trip currentTrip = (Trip) tripIDToTrip.get(tripID);
                    currentTrip.addNewStop(stopID, arrivalTime, departureTime);
                } else {
                    System.out.println("trip ID not found in tripIDToTrip");
                    break;
                }

                // build stopIDToTripIDSortedByTime map
                if (stopIDToTripIDSortedByTime.containsKey(stopID)) {
                    Map<LocalDateTime, List<Integer>> tm = stopIDToTripIDSortedByTime.get(stopID);
                    if (tm.containsKey(arrivalTime)) {
                        tm.get(arrivalTime).add(tripID);
                    } else {
                        List<Integer> l = new ArrayList<>();
                        l.add(tripID);
                        tm.put(arrivalTime, l);
                    }
                } else {
                    Map<LocalDateTime, List<Integer>> tm = new TreeMap<>();
                    List<Integer> l = new ArrayList<>();
                    l.add(tripID);
                    tm.put(arrivalTime, l);
                    stopIDToTripIDSortedByTime.put(stopID, tm);
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tripIDToTrip;
    }

    @Override
    public List<ITrip> findEarliestTrip(Integer sourceStopID, Integer destinationStopID,
            LocalDateTime currentTime) {
        // store the result
        List<ITrip> earliestTrip = new ArrayList<>();

        // A custom comparator that compares two Trips by arrival time at the
        // destination
        Comparator<ITrip> customComparator = new Comparator<ITrip>() {
            @Override
            public int compare(ITrip t1, ITrip t2) {
                LocalDateTime t1Dest = t1.arriveAt(destinationStopID);
                LocalDateTime t2Dest = t2.arriveAt(destinationStopID);
                if (t1Dest.isBefore(t2Dest)) {
                    return -1;
                } else if (t1Dest.isAfter(t2Dest)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        // Create a Priority Queue with a custom Comparator
        PriorityQueue<ITrip> potentialTrips = new PriorityQueue<>(customComparator);

        for (ITrip trip : tripIDToTrip.values()) {
            // potential trips have to include both the source the destination stops
            if (trip.StopsOnTheTrip().contains(sourceStopID)
                    && trip.StopsOnTheTrip().contains(destinationStopID)) {
                LocalDateTime timeDepartFromSource = trip.departFrom(sourceStopID);
                LocalDateTime timeArriveAtDest = trip.arriveAt(destinationStopID);
                // potential trips' source time >= current time
                // and dest time > source time
                if ((timeDepartFromSource.isEqual(currentTime)
                        || timeDepartFromSource.isAfter(currentTime))
                        && timeArriveAtDest.isAfter(timeDepartFromSource)) {
//                    System.out.printf("Route: %s, departure time: %s, arrival time: %s\n",
//                            trip.getRouteId(), trip.arriveAt(sourceStopID),
//                            trip.arriveAt(destinationStopID));
                    potentialTrips.add(trip);
                }
            }
        }

        // no potential trips
        if (potentialTrips.isEmpty()) {
            return earliestTrip;
        }

        // pop the trip with the earliest departure time
        ITrip candidate = potentialTrips.poll();
        earliestTrip.add(candidate);
        // add the trips with tied departure time
        while (!potentialTrips.isEmpty() && candidate.arriveAt(destinationStopID)
                .isEqual(potentialTrips.peek().arriveAt(destinationStopID))) {
            earliestTrip.add(potentialTrips.poll());
        }
        return earliestTrip;
    }

    @Override
    public ITrip getTrip(int tripID) {
        return tripIDToTrip.get(tripID);
    }

    @Override
    public Map<Integer, Map<LocalDateTime, List<Integer>>> getStopIDToTripIDMap() {
        return stopIDToTripIDSortedByTime;
    }

    @Override
    public List<SimpleEntry<Integer, LocalDateTime>> findStationAndArrivalTime(int tripID,
            LocalDateTime begin, LocalDateTime end) {
        // list to store the result
        List<SimpleEntry<Integer, LocalDateTime>> result = new ArrayList<>();
        // get the Trip object given a trip ID
        ITrip trip = tripIDToTrip.get(tripID);
        // loop through the stops on the trip and find the stops within the time range
        for (Integer stopID : trip.StopsOnTheTrip()) {
            if ((trip.arriveAt(stopID).isAfter(begin) || trip.arriveAt(stopID).equals(begin))
                    && trip.arriveAt(stopID).isBefore(end) || (trip.arriveAt(stopID).equals(end))) {
                result.add(new SimpleEntry<Integer, LocalDateTime>(stopID, trip.arriveAt(stopID)));
            }
        }
        return result;
    }
}
