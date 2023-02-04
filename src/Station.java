/**
 * Implementation of IStation interface
 * 
 * @author Zheyi Chen
 *
 */
public class Station implements IStation {

    private int stationID;
    private String stationName;
    private double latitude;
    private double longitude;
    private int wheelChair;

    /**
     * Constructor for Station
     * 
     * @param stopID     - ID of the station
     * @param stopName   - station name
     * @param latitude   - latitude of location
     * @param longitude  - longitude of location
     * @param wheelChair - wheel chair access
     */
    public Station(int stopID, String stopName, double latitude, double longitude, int wheelChair) {
        this.stationID = stopID;
        this.stationName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.wheelChair = wheelChair;
    }

    @Override
    public String getStationName() {
        return stationName;
    }

    @Override
    public int getStationID() {
        return stationID;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public boolean hasWheelChairAccess() {
        // 1 represents it has wheelChair on-boarding access
        // 0 means no, 2 means unknown
        // so we only treat cases where we can confirm there is accessability
        if (this.wheelChair == 1) {
            return true;
        } else {
            return false;
        }
    }

}
