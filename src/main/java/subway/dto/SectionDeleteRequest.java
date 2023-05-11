package subway.dto;

import javax.validation.constraints.NotNull;

public class SectionDeleteRequest {

    @NotNull(message = "노선 id는 필수로 입력해야합니다.")
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
