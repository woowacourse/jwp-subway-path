package subway.presentation.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import subway.domain.section.Direction;

public class CreateSectionRequest {

    @Positive(message = "역의 식별자는 양수여야 합니다.")
    private Long sourceStationId;

    @Positive(message = "역의 식별자는 양수여야 합니다.")
    private Long targetStationId;

    @Positive(message = "역 간의 거리는 양수여야 합니다.")
    private int distance;

    @NotNull(message = "방향은 필수적으로 입력해야 합니다.")
    private Direction direction;

    private CreateSectionRequest() {
    }

    private CreateSectionRequest(
            final Long sourceStationId,
            final Long targetStationId,
            final int distance,
            final Direction direction
    ) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.distance = distance;
        this.direction = direction;
    }

    public static CreateSectionRequest of(
            final Long sourceStationId,
            final Long targetStationId,
            final int distance,
            final Direction direction
    ) {
        return new CreateSectionRequest(sourceStationId, targetStationId, distance, direction);
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Direction getDirection() {
        return direction;
    }
}
