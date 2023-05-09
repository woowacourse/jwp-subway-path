package subway.domain.station;

public class StationOnLine {
    private final Station station;
    private final StationDistance previousStationDistance;
    private final StationDistance nextStationDistance;

    public StationOnLine(final Station station, final StationDistance previousStationDistance,
                         final StationDistance nextStationDistance) {
        this.station = station;
        this.previousStationDistance = previousStationDistance;
        this.nextStationDistance = nextStationDistance;
    }

    public Station getStation() {
        return station;
    }

    public StationDistance getPreviousStationDistance() {
        return previousStationDistance;
    }

    public StationDistance getNextStationDistance() {
        return nextStationDistance;
    }
}
