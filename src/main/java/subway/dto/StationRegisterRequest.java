package subway.dto;

public class StationRegisterRequest {
    private Long upperStation;
    private Long lowerStation;
    private Integer distance;

    public StationRegisterRequest(final Long upperStation, final Long lowerStation, final Integer distance) {
        this.upperStation = upperStation;
        this.lowerStation = lowerStation;
        this.distance = distance;
    }

    public Long getUpperStation() {
        return upperStation;
    }

    public Long getLowerStation() {
        return lowerStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
