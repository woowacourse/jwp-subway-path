package subway.dto;

import java.util.List;

public class LineResponse {

    private final List<String> stationNames;
    private final String lineName;

    public LineResponse(List<String> stationNames, String lineName) {
        this.stationNames = stationNames;
        this.lineName = lineName;
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public String getLineName() {
        return lineName;
    }
}
