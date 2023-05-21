package subway.dto;

import java.util.List;

public class PathResponse {

    private final List<String> stations;

    public PathResponse(List<String> stations) {
        this.stations = stations;
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "stations=" + stations +
                '}';
    }

    public List<String> getStations() {
        return stations;
    }
}
