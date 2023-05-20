package subway.line.presentation.dto;

import subway.line.domain.Line;
import subway.station.domain.Station;
import subway.station.presentation.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineStationResponse {

    private final Long lineId;
    private final String name;
    private final String color;
    private List<StationResponse> stationResponse;

    public LineStationResponse(final Long lineId, final String name, final String color, final List<StationResponse> stationResponse) {
        this.lineId = lineId;
        this.name = name;
        this.color = color;
        this.stationResponse = stationResponse;
    }

    public static LineStationResponse from(final Line line, final List<Station> stations) {
        return new LineStationResponse(
                line.getId(),
                line.getNameValue(),
                line.getColorValue(),
                stations.stream()
                        .map(StationResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public Long getLineId() {
        return lineId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStationResponse() {
        return stationResponse;
    }

}
