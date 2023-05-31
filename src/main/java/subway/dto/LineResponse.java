package subway.dto;

import java.util.List;

public class LineResponse {

    private final long lineId;
    private final List<String> stationNames;
    private final String lineName;

    public LineResponse(long lineId, List<String> stationNames, String lineName) {
        this.lineId = lineId;
        this.stationNames = stationNames;
        this.lineName = lineName;
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public String getLineName() {
        return lineName;
    }

    public long getLineId() {
        return lineId;
    }

    @Override
    public String toString() {
        return "LineResponse{" +
                "stationNames=" + stationNames +
                ", lineName='" + lineName + '\'' +
                '}';
    }
}
