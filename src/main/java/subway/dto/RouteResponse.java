package subway.dto;

import java.util.List;

public class RouteResponse {
    private List<String> station;
    private int fee;

    private RouteResponse() {
    }

    public RouteResponse(List<String> station, int fee) {
        this.station = station;
        this.fee = fee;
    }

    public List<String> getRoute() {
        return station;
    }

    public int getFee() {
        return fee;
    }
}
