package subway.dto.section;

public class SectionCreateRequest {

    private String lineName;
    private String upStation;
    private String downStation;
    private Long distance;

    private SectionCreateRequest() {
    }

    public SectionCreateRequest(final String lineName, final String upStation, final String downStation, final Long distance) {
        this.lineName = lineName;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
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
