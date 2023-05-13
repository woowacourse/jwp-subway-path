package subway.ui.dto;

import java.util.List;

public class LineDto {

    private final List<String> stationNames;
    private final String lineName;

    public LineDto(List<String> stationNames, String lineName) {
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
