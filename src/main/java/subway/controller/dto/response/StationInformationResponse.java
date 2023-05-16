package subway.controller.dto.response;

import subway.domain.Line;
import subway.domain.Station;
import subway.domain.StationInformation;

public class StationInformationResponse {

    private Long stationId;
    private String stationName;
    private Long lineId;
    private String lineName;
    private String lineColor;

    public StationInformationResponse() {
    }

    public StationInformationResponse(final Long stationId, final String stationName, final Long lineId,
        final String lineName, final String lineColor) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
    }

    public static StationInformationResponse of(final StationInformation stationInformation) {
        final Station station = stationInformation.getStation();
        final Line line = stationInformation.getLine();
        return new StationInformationResponse(station.getId(), station.getName(), line.getId(), line.getName(),
            line.getColor());
    }

    public Long getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
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
}
