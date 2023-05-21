package subway.ui.dto.response;

import java.util.List;

public class PathResponse {

    private int fare;
    private int distance;
    private List<StationInfo> stations;

    private PathResponse() {
    }

    public PathResponse(final int fare, final int distance, final List<StationInfo> stations) {
        this.fare = fare;
        this.distance = distance;
        this.stations = stations;
    }

    public int getFare() {
        return fare;
    }

    public int getDistance() {
        return distance;
    }

    public List<StationInfo> getStations() {
        return stations;
    }
}
