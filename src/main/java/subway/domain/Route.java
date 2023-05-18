package subway.domain;

import java.util.List;

public class Route {
    private final Long lineId;
    private final String lineName;
    private final List<Station> stations;

    public Route(Long lineId, String lineName, List<Station> stations) {
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
}
