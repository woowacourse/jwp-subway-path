package subway.dto.request;

public class CreateSectionRequest {

    private Long upStation;
    private Long downStation;
    private Integer distance;

    public CreateSectionRequest() {
    }

    public CreateSectionRequest(final Long upStation, final Long downStation, final Integer distance) {
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
