package subway.dto.response;

import java.util.List;

public class LineStationsResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stationResponses;

    public LineStationsResponse(Long id, String name, String color, List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
