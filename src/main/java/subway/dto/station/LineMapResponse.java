package subway.dto.station;

import java.util.List;

public class LineMapResponse {

    private final List<StationResponse> stations;

    public LineMapResponse(final List<StationResponse> stations) {
        this.stations = stations;
    }

    public static LineMapResponse from(final List<StationResponse> stations) {
        return new LineMapResponse(stations);
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
