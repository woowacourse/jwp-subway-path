package subway.dto;

public class SectionDeleteRequest {

    private Long lineId;

    private SectionDeleteRequest() {
    }

    public SectionDeleteRequest(Long lineId) {
        this.lineId = lineId;
    }

    public Long getLineId() {
        return lineId;
    }
}
