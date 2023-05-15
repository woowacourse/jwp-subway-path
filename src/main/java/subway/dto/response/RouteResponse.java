package subway.dto.response;

import java.util.List;

public class RouteResponse {

    private Long money;
    private Integer distance;
    private List<StationResponse> stations;

    public RouteResponse() {
    }

    public RouteResponse(Long money, Integer distance, List<StationResponse> stations) {
        this.money = money;
        this.distance = distance;
        this.stations = stations;
    }

    public Long getMoney() {
        return money;
    }

    public Integer getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
