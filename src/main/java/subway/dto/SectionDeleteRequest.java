package subway.dto;

import javax.validation.constraints.NotNull;

public class SectionDeleteRequest {

    @NotNull(message = "노선 id는 필수로 입력해야합니다.")
    private Long lineId;
    @NotNull(message = "노선 id는 필수로 입력해야합니다.")
    private Long stationId;

    private SectionDeleteRequest() {
    }

    public SectionDeleteRequest(Long lineId, Long stationId) {
        this.lineId = lineId;
        this.stationId = stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getStationId() {
        return stationId;
    }
}
