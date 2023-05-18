package subway.domain;

import java.util.List;

public class LineInPath {
    private final Long lineId;
    private final String lineName;
    private final List<Station> stations;

    public LineInPath(Long lineId, String lineName, List<Station> stations) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.stations = stations;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getLineName() {
        return lineName;
    }

    public List<Station> getStations() {
        return stations;
    }

    @Override
    public String toString() {
        return "LineInPath{" +
                "lineId=" + lineId +
                ", lineName='" + lineName + '\'' +
                ", stations=" + stations +
                '}';
    }
}
