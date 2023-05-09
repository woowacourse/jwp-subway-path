package subway.dto;

public class SectionDeleteRequest {
    private String station;

    public SectionDeleteRequest() {
    }

    public SectionDeleteRequest(String station) {
        this.station = station;
    }

    public String getStation() {
        return station;
    }
}
