package subway.dto.response;

import java.util.List;

public class PathResponse {

    private final List<String> stations;
    private final int fare;
    private final int distance;

    public PathResponse(final List<String> stations, final int fare, final int distance) {
        this.stations = stations;
        this.fare = fare;
        this.distance = distance;
    }
}
