package subway.dto;

import java.util.List;

public class RouteDto {

    private final DistanceDto distance;
    private final FeeDto fee;
    private final List<String> stations;

    public RouteDto(final DistanceDto distance, final FeeDto fee, final List<String> stations) {
        this.distance = distance;
        this.fee = fee;
        this.stations = stations;
    }

    public DistanceDto getDistance() {
        return distance;
    }

    public FeeDto getFee() {
        return fee;
    }

    public List<String> getStations() {
        return stations;
    }

}
