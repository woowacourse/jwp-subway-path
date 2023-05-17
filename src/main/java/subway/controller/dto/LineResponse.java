package subway.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;

public class LineResponse {

    private Long id;
    private String name;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(Long id, String name, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        List<Station> stationList = line.findLeftToRightRoute();
        List<StationResponse> stationResponses = stationList.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), stationResponses);
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
