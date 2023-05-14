package subway.ui.line.dto;

public class LineCreateRequest {
    private final String lineName;
    private final String upStation;
    private final String downStation;
    private final Integer distance;

    public LineCreateRequest(final String lineName, final String upStation, final String downStation, final Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }
}