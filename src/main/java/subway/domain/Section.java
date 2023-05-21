package subway.domain;

public class Section {
    private final Long id;
    private final Station startStation;
    private final Station endStation;
    private final Distance distance;

    public Section(Station startStation, Station endStation, Distance distance) {
        this(null, startStation, endStation, distance);
    }

    public Section(Long id, Station startStation, Station endStation, Distance distance) {
        this.id = id;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
    }

    public boolean hasStation(Station station) {
        return startStation.equals(station) || endStation.equals(station);
    }

    public boolean hasStationInStartPosition(Station station) {
        return startStation.equals(station);
    }

    public boolean hasStationInEndPosition(Station station) {
        return endStation.equals(station);
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public Long getStartStationId() {
        return startStation.getId();
    }

    public Long getEndStationId() {
        return endStation.getId();
    }

    public Distance getDistance() {
        return distance;
    }

    public double getDistanceByValue() {
        return distance.value();
    }

    public Long getId() {
        return id;
    }
}
