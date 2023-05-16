package subway.controller.dto;

public class SectionDeleteRequest {

    private String stationName;

    private SectionDeleteRequest() {
    }

    public SectionDeleteRequest(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
