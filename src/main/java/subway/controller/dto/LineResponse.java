package subway.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.line.Line;
import subway.domain.line.Station;

public class LineResponse {

    private Long id;
    private String name;
    private Integer extraFare;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    public LineResponse(Long id, String name, Integer extraFare, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.extraFare = extraFare;
        this.stations = stations;
    }

    public static LineResponse of(Line line) {
        List<Station> stationList = line.findLeftToRightRoute();
        List<StationResponse> stationResponses = stationList.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getExtraFare(), stationResponses);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getExtraFare() {
        return extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
