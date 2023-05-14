package subway.ui.dto.request;

public class SectionDeleteRequest {
    private String stationName;

    public SectionDeleteRequest() {
    }

    public SectionDeleteRequest(final String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
