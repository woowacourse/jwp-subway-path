package subway.ui.route.find.dto;

import java.util.List;

public class RouteFindResponse {

    private int distance;
    private int fare;
    private List<Long> stations;

    private RouteFindResponse() {
    }

    public RouteFindResponse(final List<Long> stations, final int distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public List<Long> getStations() {
        return stations;
    }
}
