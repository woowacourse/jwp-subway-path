package subway.dto.response;

import java.util.List;

public class RouteResponse {

    private Long fare;
    private Integer distance;
    private List<StationResponse> stations;

    public RouteResponse() {
    }

    public RouteResponse(Long fare, Integer distance, List<StationResponse> stations) {
        this.fare = fare;
        this.distance = distance;
        this.stations = stations;
    }

    public Long getFare() {
        return fare;
    }

    public Integer getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
