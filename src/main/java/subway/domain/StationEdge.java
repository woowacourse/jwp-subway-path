package subway.domain;

public class StationEdge {

    private final Station destination;
    private final int distance;

    public StationEdge(final Station station, final int distance) {
        this.destination = station;
        this.distance = distance;
    }


    public Station getDestination() {
        return destination;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "StationEdge{" +
                "destination=" + destination +
                ", distance=" + distance +
                '}';
    }
}
