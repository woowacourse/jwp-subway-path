package subway.dto;

import java.util.List;

public class RouteDto {

    private final int distance;
    private final int fee;
    private final List<String> stations;

    public RouteDto(final int distance, final int fee, final List<String> stations) {
        this.distance = distance;
        this.fee = fee;
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }

    public List<String> getStations() {
        return stations;
    }

}
