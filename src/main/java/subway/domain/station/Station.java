package subway.domain.station;

public class Station {
    private final StationName stationName;
    private final StationDistance previousStationDistance;
    private final StationDistance nextStationDistance;

    public Station(final StationName stationName, final StationDistance previousStationDistance,
                   final StationDistance nextStationDistance) {
        this.stationName = stationName;
        this.previousStationDistance = previousStationDistance;
        this.nextStationDistance = nextStationDistance;
    }
}
