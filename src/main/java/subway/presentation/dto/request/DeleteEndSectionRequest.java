package subway.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class DeleteEndSectionRequest {

    private final Long targetStationId;

    @JsonCreator
    public DeleteEndSectionRequest(final Long targetStationId) {
        this.targetStationId = targetStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
