package subway.dto;

import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(final Long id, final String name, final List<StationResponse> stations) {
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
