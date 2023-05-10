package subway.dto.section;

public class SectionCreateRequest {

    private final String lineName;
    private final String upStation;
    private final String downStation;
    private final Long distance;

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
