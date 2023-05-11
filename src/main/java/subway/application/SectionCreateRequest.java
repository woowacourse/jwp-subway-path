package subway.application;

public class SectionCreateRequest {
    private String upStation;
    private String downStation;

    private Long distance;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(final String upStation, final String downStation, final Long distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }

    public Long getDistance() {
        return distance;
    }
}
