package subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import subway.dto.LineCreateDto;

public class LineCreateRequest {
    @NotBlank(message = "노선의 이름은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private String lineName;

    @NotNull(message = "노선의 추가 금액은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    @PositiveOrZero(message = "노선의 추가 금액은 0이거나 양수여야 합니다. 입력값 : ${validatedValue}")
    private Integer extraCharge;

    @NotNull(message = "상행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long upStationId;

    @NotNull(message = "하행역은 비어있을 수 없습니다. 입력값 : ${validatedValue}")
    private Long downStationId;

    @NotNull(message = "역 간 거리는 비어있을 수 없습니다. 입력값 : ${validatedValue}}")
    @Positive(message = "역 간 거리는 양수여야 합니다. 입력값 : ${validatedValue}")
    private Integer distance;

    public LineCreateRequest(String lineName, Integer extraCharge, Long upStationId, Long downStationId,
                             Integer distance) {
        this.lineName = lineName;
        this.extraCharge = extraCharge;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineCreateDto toDto() {
        return new LineCreateDto(lineName, extraCharge, upStationId, downStationId, distance);
    }

    public String getLineName() {
        return lineName;
    }

    public Integer getExtraCharge() {
        return extraCharge;
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
}
