package subway.application;

public class SectionDeleteRequest {
    private String upStation;
    private String downStation;

    public SectionDeleteRequest() {
    }

    public SectionDeleteRequest(final String upStation, final String downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }
}
