package subway.dto;

import java.util.List;

public class LineFindResponse {

    private final String lineName;
    private final List<String> stationNames;

    public LineFindResponse(String lineName, List<String> stationNames) {
        this.lineName = lineName;
        this.stationNames = stationNames;
    }

    public String getLineName() {
        return lineName;
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    @Override
    public String toString() {
        return "LineFindResponse{" +
                "lineName='" + lineName + '\'' +
                ", stationNames=" + stationNames +
                '}';
    }
}
