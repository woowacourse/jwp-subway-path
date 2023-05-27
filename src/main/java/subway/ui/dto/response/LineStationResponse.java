package subway.ui.dto.response;

import java.util.List;

public class LineStationResponse {

    private Long id;
    private String name;
    private List<StationResponse> stations;

    private LineStationResponse() {
    }

    public LineStationResponse(
            final Long id,
            final String name,
            final List<StationResponse> stations
    ) {
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
