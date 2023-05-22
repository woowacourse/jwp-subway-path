package subway.controller.dto.request;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class FarePolicyRequest {

    @Positive
    private final Long lineId;

    @PositiveOrZero
    private final Integer additionalFare;

    public FarePolicyRequest(Long lineId, Integer additionalFare) {
        this.lineId = lineId;
        this.additionalFare = additionalFare;
    }

    public Long getLineId() {
        return lineId;
    }

    public Integer getAdditionalFare() {
        return additionalFare;
    }

}
