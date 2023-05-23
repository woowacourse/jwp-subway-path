package subway.domain;

import java.util.List;

public class Route {
    private final List<Station> stations;
    private final Distance distance;
    private final Fee fee;

    public Route(final List<Station> stations, final Distance distance, final Fee fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public Fee getFee() {
        return fee;
    }

}
