package subway.controller.dto.response;

import subway.service.domain.FarePolicy;

public class FarePolicyResponse {

    private final Long id;
    private final LinePropertyResponse linePropertyResponse;
    private final Integer additionalFare;

    private FarePolicyResponse(Long id, LinePropertyResponse linePropertyResponse, Integer additionalFare) {
        this.id = id;
        this.linePropertyResponse = linePropertyResponse;
        this.additionalFare = additionalFare;
    }

    public static FarePolicyResponse from(FarePolicy farePolicy) {
        return new FarePolicyResponse(
                farePolicy.getId(),
                LinePropertyResponse.from(farePolicy.getLineProperty()),
                farePolicy.getAdditionalFare()
        );
    }

    public Long getId() {
        return id;
    }

    public LinePropertyResponse getLinePropertyResponse() {
        return linePropertyResponse;
    }

    public Integer getAdditionalFare() {
        return additionalFare;
    }

}
