package subway.dto;

public class StationAddToLineRequest {
    private final String station;
    private final String adjacentStation;
    private final String addDirection;
    private final Integer distance;

    public StationAddToLineRequest(String station, String adjacentStation, String addDirection, Integer distance) {
        this.station = station;
        this.adjacentStation = adjacentStation;
        this.addDirection = addDirection;
        this.distance = distance;
    }

    public String getStation() {
        return station;
    }

    public String getAdjacentStation() {
        return adjacentStation;
    }

    public String getAddDirection() {
        return addDirection;
    }

    public Integer getDistance() {
        return distance;
    }
}
