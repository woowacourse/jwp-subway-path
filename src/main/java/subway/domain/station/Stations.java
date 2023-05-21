package subway.domain.station;

import subway.domain.Distance;

public class Stations {

    private final Station current;
    private final Station next;
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

    public boolean isSame(final Stations stations) {
        return current.isSame(stations.current)
                && next.isSame(stations.next);
    }

    public void updateStationOnAdd(final Stations newStations) {
        current.updateStationName(newStations.next);
        distance = distance.minus(newStations.distance);
    }

    public void updateStationOnDelete(final Stations deleteStations) {
        next.updateStationName(deleteStations.next);
        distance = distance.plus(deleteStations.distance);
    }

    public Stations cloneStations() {
        return new Stations(current.cloneStation(), next.cloneStation(), distance.getValue());
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
