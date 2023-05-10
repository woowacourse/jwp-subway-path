package subway.domain;

public class Stations {

    private Station current;
    private Station next;
    private int distance;

    public Stations(final Station current, final Station next, final int distance) {
        this.current = current;
        this.next = next;
        this.distance = distance;
    }

    public boolean isLinked(final Stations other) {
        return next.isSame(other.current);
    }

    public boolean isDistanceShorterThan(final Stations other) {
        return distance <= other.distance;
    }

    public void updateStationOnAdd(final Stations newStations) {
        current.updateStationName(newStations.next);
        distance = distance - newStations.distance;
    }

    public void updateStationOnDelete(final Stations deleteStations) {
        next.updateStationName(deleteStations.next);
        distance = distance + deleteStations.distance;
    }

    public int getDistance() {
        return distance;
    }

    public Station getNext() {
        return next;
    }
}
