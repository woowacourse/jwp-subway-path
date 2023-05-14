package subway.presentation.dto;

public class StationEnrollRequest {

    private Long upBound;
    private Long downBound;
    private Integer distance;

    public StationEnrollRequest() {
    }

    public StationEnrollRequest(Long upBound, Long downBound, Integer distance) {
        this.upBound = upBound;
        this.downBound = downBound;
        this.distance = distance;
    }

    public Long getUpBound() {
        return upBound;
    }

    public Long getDownBound() {
        return downBound;
    }

    public Integer getDistance() {
        return distance;
    }
}
