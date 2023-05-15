package subway.ui.dto.request;

import javax.validation.constraints.Positive;

public class DeleteSectionRequest {

    @Positive(message = "역의 식별자는 양수여야 합니다.")
    private Long targetStationId;

    private DeleteSectionRequest() {
    }

    public static DeleteSectionRequest of(final Long targetStationId) {
        return new DeleteSectionRequest(targetStationId);
    }

    public DeleteSectionRequest(final Long targetStationId) {
        this.targetStationId = targetStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }
}
