package subway.adapter.in.web.section.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import subway.application.port.in.section.dto.command.AddStationToLineCommand;

public class AddStationToLineRequest {

    @NotNull(message = "상행역 id가 없습니다.")
    private Long upStationId;

    @NotNull(message = "하행역 id가 없습니다.")
    private Long downStationId;

    @NotNull(message = "거리 정보가 없습니다.")
    @Positive(message = "두 역간 거리는 양의 정수여야합니다.")
    private Integer distance;

    private AddStationToLineRequest() {
    }

    public AddStationToLineRequest(final Long upStationId, final Long downStationId, final Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public AddStationToLineCommand toCommand(final long lineId) {
        return new AddStationToLineCommand(lineId, upStationId, downStationId, distance);
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
