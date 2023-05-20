package subway.ui.dto;

import java.util.List;

import subway.domain.Line;
import subway.domain.Station;

public class GetLineResponse {

    private final Long lineId;
    private final String lineName;
    private final String lineColor;
    private final List<StationResponse> stations;

    public GetLineResponse(Long lineId, String lineName, String lineColor, List<StationResponse> stations) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
        this.stations = stations;
    }

    public static GetLineResponse from(Line line, List<Station> stations) {
        return new GetLineResponse(line.getId(), line.getName(), line.getColor(),
            StationResponse.toStationResponses(stations));
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public String getLineColor() {
        return lineColor;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
