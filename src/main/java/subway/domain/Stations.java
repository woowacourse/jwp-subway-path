package subway.domain;

public class Stations {

    private Station current;
    private Station next;
    private Distance distance;

    public Stations(final Station current, final Station next, final int distance) {
        this.current = current;
        this.next = next;
        this.distance = new Distance(distance);
    }

    public boolean isLinked(final Stations other) {
        return next.isSame(other.current);
    }

    public boolean isSameCurrent(final Station other) {
        return current.isSame(other);
    }

    public boolean isSameNext(final Station other) {
        return next.isSame(other);
    }

    public boolean isDistanceShorterThan(final Stations other) {
        return distance.isShorterThan(other.distance);
    }

    public void updateStationOnAdd(final Stations newStations) {
        current.updateStationName(newStations.next);
        distance = distance.minus(newStations.distance);
    }

    public void updateStationOnDelete(final Stations deleteStations) {
        next.updateStationName(deleteStations.next);
        distance = distance.plus(deleteStations.distance);
    }

    public int getDistance() {
        return distance.getValue();
    }

    public Station getCurrent() {
        return current;
    }

    public Station getNext() {
        return next;
    }
}
