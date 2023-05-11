package subway.dto;

public class StationAddToLineRequest {
    private final String station;
    private final String neighborhoodStation;
    private final String addDirection;
    private final Integer distance;

    public StationAddToLineRequest(String station, String neighborhoodStation, String addDirection, Integer distance) {
        this.station = station;
        this.neighborhoodStation = neighborhoodStation;
        this.addDirection = addDirection;
        this.distance = distance;
    }

    public String getStation() {
        return station;
    }

    public String getNeighborhoodStation() {
        return neighborhoodStation;
    }

    public String getAddDirection() {
        return addDirection;
    }

    public Integer getDistance() {
        return distance;
    }
}
