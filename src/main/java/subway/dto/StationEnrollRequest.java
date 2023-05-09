package subway.dto;

public class StationEnrollRequest {

    private Integer fromStation;
    private Integer toStation;
    private Integer distance;

    public StationEnrollRequest() {
    }

    public StationEnrollRequest(Integer fromStation, Integer toStation, Integer distance) {
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.distance = distance;
    }

    public Integer getFromStation() {
        return fromStation;
    }

    public Integer getToStation() {
        return toStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
