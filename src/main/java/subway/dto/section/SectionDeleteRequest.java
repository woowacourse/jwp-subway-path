package subway.dto.section;

public class SectionDeleteRequest {

    private Long lineNumber;
    private String station;

    private SectionDeleteRequest() {
    }

    public SectionDeleteRequest(final Long lineNumber, final String station) {
        this.lineNumber = lineNumber;
        this.station = station;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public String getStation() {
        return station;
    }
}
