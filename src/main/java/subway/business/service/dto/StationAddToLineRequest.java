package subway.business.service.dto;

public class StationAddToLineRequest {
    private final Long stationId;
    private final Long neighborhoodStationId;
    private final String addDirection;
    private final Integer distance;

    public StationAddToLineRequest(Long stationId, Long neighborhoodStationId, String addDirection, Integer distance) {
        this.stationId = stationId;
        this.neighborhoodStationId = neighborhoodStationId;
        this.addDirection = addDirection;
        this.distance = distance;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getNeighborhoodStationId() {
        return neighborhoodStationId;
    }

    public String getAddDirection() {
        return addDirection;
    }

    public Integer getDistance() {
        return distance;
    }
}
