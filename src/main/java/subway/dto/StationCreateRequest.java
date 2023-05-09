package subway.dto;

public class StationCreateRequest {
    private final String lineName;
    private final String stationName;
    private final String adjacentStationName;
    private final boolean isUpLine;
    private final double distance;

    public StationCreateRequest(final String lineName,
                                final String stationName,
                                final String adjacentStationName,
                                final boolean isUpLine,
                                final double distance) {
        this.lineName = lineName;
        this.stationName = stationName;
        this.adjacentStationName = adjacentStationName;
        this.isUpLine = isUpLine;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public String getAdjacentStationName() {
        return adjacentStationName;
    }

    public boolean isUpLine() {
        return isUpLine;
    }

    public double getDistance() {
        return distance;
    }
}
