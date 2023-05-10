package subway.dto;

public class StationAddToLineRequest {
    private final String station;
    private final String adjacentStation;
    private final String add_direction;
    private final Integer distance;

    public StationAddToLineRequest(String station, String adjacentStation, String add_direction, Integer distance) {
        this.station = station;
        this.adjacentStation = adjacentStation;
        this.add_direction = add_direction;
        this.distance = distance;
    }

    public String getStation() {
        return station;
    }

    public String getAdjacentStation() {
        return adjacentStation;
    }

    public String getAdd_direction() {
        return add_direction;
    }

    public Integer getDistance() {
        return distance;
    }
}
