
public class TripStop {
    private String location;
    private int distance;
    private String activity;

    public TripStop(String l) {
        this.location = l;
    }

    public TripStop(String location, int distance, String activity) throws IllegalArgumentException {
        if(distance < 0) {
            throw new IllegalArgumentException("Distance cannot be less than zero");
        }
        this.location = location;
        this.distance = distance;
        this.activity = activity;
    }

    public TripStop() {
        this.location = "";
        this.distance = 0;
        this.activity = "";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}