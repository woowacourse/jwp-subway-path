package subway.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreationEndSectionRequest {

    private final Long sourceStationId;
    private final Long targetStationId;
    private final int distance;

    @JsonCreator
    public CreationEndSectionRequest(@JsonProperty(value = "sourceStationId") final Long sourceStationId,
            @JsonProperty(value = "targetStationId") final Long targetStationId,
            @JsonProperty(value = "distance") final int distance) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
        this.distance = distance;
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
}
