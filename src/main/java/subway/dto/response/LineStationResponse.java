package subway.dto.response;

import java.util.Objects;
import subway.domain.subwaymap.Line;
import subway.domain.subwaymap.LineStation;
import subway.domain.subwaymap.Station;

public class LineStationResponse {

    private Long stationId;
    private String name;
    private Long lineId;
    private String lineName;
    private String lineColor;

    LineStationResponse() {

    }

    public LineStationResponse(final Long stationId, final String name, final Long lineId, final String lineName,
        final String lineColor) {
        this.stationId = stationId;
        this.name = name;
        this.lineId = lineId;
        this.lineName = lineName;
        this.lineColor = lineColor;
    }

    public static LineStationResponse of(final LineStation lineStation) {
        final Line line = lineStation.getLine();
        final Station station = lineStation.getStation();
        return new LineStationResponse(station.getId(), station.getName(), line.getId(), line.getName(),
            line.getColor());
    }

    public Long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LineStationResponse that = (LineStationResponse) o;
        return Objects.equals(stationId, that.stationId) && Objects.equals(lineId, that.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationId, lineId);
    }
}

