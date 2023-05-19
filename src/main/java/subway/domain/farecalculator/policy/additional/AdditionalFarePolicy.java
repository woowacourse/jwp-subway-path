package subway.domain.farecalculator.policy.additional;

import java.util.List;

import subway.dto.SectionResponse;

public interface AdditionalFarePolicy {
    Integer calculateAdditionalFare(List<SectionResponse> sections);
}
