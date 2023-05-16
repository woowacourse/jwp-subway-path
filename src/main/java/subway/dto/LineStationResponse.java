package subway.dto;

import java.util.List;

public class LineStationResponse {
    private Long id;
    private final String name;
    private final List<StationResponse> stations;

    public LineStationResponse(Long id, String name, List<StationResponse> stations) {
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

    public List<StationResponse> getStations() {
        return stations;
    }
}
