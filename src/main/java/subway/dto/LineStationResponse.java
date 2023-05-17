package subway.dto;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class LineStationResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stationResponses;

    public LineStationResponse(final Long id, final String name, final String color, final List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static LineStationResponse of(final Line line, final List<Station> stations) {
        List<StationResponse> stationResponses = stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());
        return new LineStationResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stationResponses
        );
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
