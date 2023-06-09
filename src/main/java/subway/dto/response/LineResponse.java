package subway.dto.response;

import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final List<LineStationResponse> stations;

    public LineResponse(Long id, String name, List<LineStationResponse> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<LineStationResponse> getStations() {
        return stations;
    }
}
