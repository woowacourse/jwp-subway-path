package subway.domain;

public class StationEdge {

    private final Station destination;
    private final double distance;

    public StationEdge(final Station destination, final double distance) {
        this.destination = destination;
        this.distance = distance;
    }

    public Station getDestination() {
        return destination;
    }

    public double getDistance() {
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
