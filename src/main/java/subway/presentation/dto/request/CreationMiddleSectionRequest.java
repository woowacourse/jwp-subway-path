package subway.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreationMiddleSectionRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final Long targetStationId;
    private final int distance;

    @JsonCreator
    public CreationMiddleSectionRequest(@JsonProperty(value = "upStationId") final Long upStationId,
                                        @JsonProperty(value = "downStationId") final Long downStationId,
                                        @JsonProperty(value = "targetStationId") final Long targetStationId,
                                        @JsonProperty(value = "distance") final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.targetStationId = targetStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

    public int getDistance() {
        return distance;
    }
}
