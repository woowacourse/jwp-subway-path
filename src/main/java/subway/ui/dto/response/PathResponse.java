package subway.ui.dto.response;

import java.util.List;

public class PathResponse {

    private int distance;
    private List<StationInfo> stations;

    private PathResponse() {
    }

    public PathResponse(final int distance, final List<StationInfo> stations) {
        this.distance = distance;
        this.stations = stations;
    }

    public int getDistance() {
        return distance;
    }

    public List<StationInfo> getStations() {
        return stations;
    }
}
