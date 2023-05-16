package subway.application.dto;

import javax.validation.constraints.NotNull;

public class SectionRequest {

    @NotNull(message = "호선의 번호를 입력해주세요.")
    private final Long lineId;
    @NotNull(message = "노선 정보를 입력해주세요.")
    private final SectionStations section;

    public SectionRequest(final Long lineId, final SectionStations section) {
        this.lineId = lineId;
        this.section = section;
    }

    public Long leftStationId() {
        return section.getLeftStationId();
    }

    public Long rightStationId() {
        return section.getRightStationId();
    }

    public Integer distance() {
        return section.getDistance();
    }

    public Long getLineId() {
        return lineId;
    }

    public SectionStations getSection() {
        return section;
    }
}
