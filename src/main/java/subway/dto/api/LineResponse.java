package subway.dto.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.domain.Line;
import subway.domain.Station;

public class LineResponse {
    private Long id;
    private String name;
    private String color;

    private List<StationResponse> stations;

    private LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse of(Line line, Map<Long, Station> stationIdToStation) {
        List<Station> stations = line.getStationEdges().stream()
                .map(stationEdge -> {
                    Long downStationId = stationEdge.getDownStationId();
                    return stationIdToStation.get(downStationId);
                })
                .collect(Collectors.toList());
        return LineResponse.of(line, stations);
    }

    public static LineResponse of(Line line, List<Station> stations) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    public static List<LineResponse> of(List<Line> lines, Map<Long, Station> stationIdToStation) {
        return lines.stream()
                .map(line -> LineResponse.of(line, stationIdToStation))
                .collect(Collectors.toList());
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

    public List<StationResponse> getStations() {
        return stations;
    }
}
