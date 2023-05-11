package subway.ui.dto;

import java.util.List;

public class LineStationResponse {

    private Long id;
    private String name;
    private List<StationResponse> stations;

    private LineStationResponse() {
    }

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
