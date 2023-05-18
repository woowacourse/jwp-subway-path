package subway.ui.dto.request;

import javax.validation.constraints.NotNull;

public class AddSectionRequest {

    @NotNull
    private final Long upStationId;
    @NotNull
    private final Long downStationId;
    @NotNull
    private final Integer distance;

    private AddSectionRequest(final Long upStationId, final Long downStationId, final Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static AddSectionRequest of(final Long upStationId, final Long downStationId, final Integer distance) {
        return new AddSectionRequest(upStationId, downStationId, distance);
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
