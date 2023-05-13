package subway.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreationInitialSectionRequest {

    private final Long upStationId;
    private final Long downStationId;
    private final int distance;

    @JsonCreator
    public CreationInitialSectionRequest(
            @JsonProperty(value = "upStationId") final Long upStationId,
            @JsonProperty(value = "downStationId") final Long downStationId,
            @JsonProperty(value = "distance") final int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
