package subway.dto;

import subway.domain.Station;
import java.util.List;

public class RouteResponse {

    private final List<Station> stations;
    private final double distance;
    private final int fee;

    public RouteResponse(final List<Station> stations, final double distance, final int fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }
}
