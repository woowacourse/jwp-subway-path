package subway.ui.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class StationInsertRequest {

    @NotNull(message = "추가할 역의 아이디가 있어야 합니다.")
    private final Long stationId;

    @NotNull(message = "추가할 호선의 아이디가 있어야 합니다.")
    private final Long lineId;

    @NotNull(message = "인접한 역의 아이디가 있어야 합니다.")
    private final Long adjacentStationId;

    @Pattern(
            regexp = "^((UP)|(DOWN))$",
            message = "인접한 역으로부터의 방향을 UP 또는 DOWN 으로 입력해야 합니다."
    )
    private final String direction;

    @Positive(message = "역간 거리는 양의 정수여야 합니다.")
    private final Integer distance;

    public StationInsertRequest(
            final Long stationId,
            final Long lineId,
            final Long adjacentStationId,
            final String direction,
            final Integer distance
    ) {
        this.stationId = stationId;
        this.lineId = lineId;
        this.adjacentStationId = adjacentStationId;
        this.direction = direction;
        this.distance = distance;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getAdjacentStationId() {
        return adjacentStationId;
    }

    public String getDirection() {
        return direction;
    }

    public Integer getDistance() {
        return distance;
    }
}
