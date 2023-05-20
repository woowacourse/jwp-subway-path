package subway.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Schema(
        description = "구간 정보 생성 요청 정보",
        example = "{\"upwardStationId\": 1, \"downwardStationId\": 2, \"distance\": 5}"
)
public class SectionCreateRequest {

    @Schema(description = "상행 역 ID")
    @NotNull(message = "상행 역 ID는 존재해야 합니다.")
    private Long upwardStationId;

    @Schema(description = "하행 역 ID")
    @NotNull(message = "하행 역 ID는 존재해야 합니다.")
    private Long downwardStationId;

    @Schema(description = "상행 역, 하행 역 사이 거리")
    @NotNull(message = "역 간의 거리는 존재해야 합니다.")
    @Positive(message = "역 간의 거리는 0보다 커야합니다.")
    private Integer distance;

    private SectionCreateRequest() {
    }

    public SectionCreateRequest(final Long upwardStationId, final Long downwardStationId, final Integer distance) {
        this.upwardStationId = upwardStationId;
        this.downwardStationId = downwardStationId;
        this.distance = distance;
    }

    public Long getUpwardStationId() {
        return upwardStationId;
    }

    public Long getDownwardStationId() {
        return downwardStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
