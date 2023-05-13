package subway.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class DeleteMiddleSectionRequest {

    private final Long targetStationId;

    @JsonCreator
    public DeleteMiddleSectionRequest(final Long targetStationId) {
        this.targetStationId = targetStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
