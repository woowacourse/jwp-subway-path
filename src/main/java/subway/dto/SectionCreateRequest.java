package subway.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class SectionCreateRequest {
    @NotNull(message = "역 ID는 필수 입력 값입니다.")
    @Positive(message = "역 ID는 양수여야 합니다.")
    private Long upStationId;

    @NotNull(message = "역 ID는 필수 입력 값입니다.")
    @Positive(message = "역 ID는 양수여야 합니다.")
    private Long downStationId;

    @NotNull(message = "거리는 필수 입력값입니다.")
    @Positive(message = "거리는 양수여야 합니다.")
    private Integer distance;

    @NotNull(message = "노선 아이디는 필수 입력값입니다.")
    @Positive(message = "노선 아이디는 양수여야 합니다.")
    private Long lineId;

    private SectionCreateRequest() {
    }

    public SectionCreateRequest(final Long upStationId, final Long downStationId, final Integer distance,
                                final Long lineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
