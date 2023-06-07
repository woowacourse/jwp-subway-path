package subway.dto.response;

import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final List<StationResponse> stations;

    public LineResponse(Long id, String name, List<StationResponse> stations) {
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
