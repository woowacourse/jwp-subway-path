package subway.business.service.dto;

import java.util.List;

public class LineStationsResponse {
    private final Long id;
    private final String name;
    private final List<StationResponse> stations;
    private final Integer fare;

    public LineStationsResponse(Long id, String name, List<StationResponse> stations, Integer fare) {
        this.id = id;
        this.name = name;
        this.stations = stations;
        this.fare = fare;
    }

    public String getName() {
        return name;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public Integer getFare() {
        return fare;
    }
}
