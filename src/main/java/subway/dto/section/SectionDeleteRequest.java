package subway.dto.section;

public class SectionDeleteRequest {
    private String stationName;

    public SectionDeleteRequest() {
    }

    public SectionDeleteRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
