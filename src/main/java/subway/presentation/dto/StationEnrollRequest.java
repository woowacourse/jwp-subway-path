package subway.presentation.dto;

public class StationEnrollRequest {

    private Long upStation;
    private Long downStation;
    private Integer distance;

    public StationEnrollRequest() {
    }

    public StationEnrollRequest(Long upStation, Long downStation, Integer distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getUpStation() {
        return upStation;
    }

    public Long getDownStation() {
        return downStation;
    }

    public Integer getDistance() {
        return distance;
    }
}
