package subway.dto;

public class StationEnrollRequest {

    private Long fromStation;
    private Long toStation;
    private Integer distance;

    public StationEnrollRequest() {
    }

    public StationEnrollRequest(Long fromStation, Long toStation, Integer distance) {
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.distance = distance;
    }

    public Long getFromStation() {
        return fromStation;
    }

    public Long getToStation() {
        return toStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
