package subway.domain.line.dto;

import javax.validation.constraints.NotNull;

public class SectionCreateRequest {

    @NotNull
    private Long lineId;
    @NotNull
    private Long baseId;
    @NotNull
    private Long addedId;
    @NotNull
    private Boolean direction;
    @NotNull
    private Integer distance;

    private SectionCreateRequest() {
    }

    public SectionCreateRequest(final Long lineId, final Long baseId, final Long addedId, final Boolean direction, final Integer distance) {
        this.lineId = lineId;
        this.baseId = baseId;
        this.addedId = addedId;
        this.direction = direction;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getBaseId() {
        return baseId;
    }

    public Long getAddedId() {
        return addedId;
    }

    public Boolean getDirection() {
        return direction;
    }

    public Integer getDistance() {
        return distance;
    }
}
