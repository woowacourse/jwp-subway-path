package subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AddPathRequest {

    @NotNull
    @Positive
    private Long targetStationId;
    @NotNull
    @Positive
    private Long addStationId;
    @Positive
    private Integer distance;
    @NotBlank
    private String direction;

    public AddPathRequest() {
    }

    public AddPathRequest(final Long targetStationId, final Long addStationId, final Integer distance, final String direction) {
        this.targetStationId = targetStationId;
        this.addStationId = addStationId;
        this.distance = distance;
        this.direction = direction;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public Long getAddStationId() {
        return addStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getDirection() {
        return direction;
    }
}
