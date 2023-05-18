package subway.ui.route.find.dto;

import java.util.List;

public class RouteFindResponse {

    private int distance;
    private int fee;
    private List<Long> stations;

    private RouteFindResponse() {
    }

    public RouteFindResponse(final List<Long> stations, final int distance, final int fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public int getDistance() {
        return distance;
    }

    public int getFee() {
        return fee;
    }

    public List<Long> getStations() {
        return stations;
    }
}
