package subway.business.service.dto;

import java.util.List;

public class LineStationsResponse {
    private final String name;
    private final List<String> stations;

    public LineStationsResponse(String name, List<String> stations) {
        this.name = name;
        this.stations = stations;
    }

    public String getName() {
        return name;
    }

    public List<String> getStations() {
        return stations;
    }
}
