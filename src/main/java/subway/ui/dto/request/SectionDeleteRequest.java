package subway.ui.dto.request;

public class SectionDeleteRequest {

    private Long lineId;
    private String stationName;

    private SectionDeleteRequest() {
    }

    public SectionDeleteRequest(final Long lineId, final String stationName) {
        this.lineId = lineId;
        this.stationName = stationName;
    }

    public Long getLineId() {
        return lineId;
    }

    public String getStationName() {
        return stationName;
    }
}
