package subway.domain.farecalculator.policy.additional;

import java.util.List;

import org.springframework.stereotype.Component;

import subway.dto.SectionResponse;

@Component
public class LineAdditionalFarePolicy implements AdditionalFarePolicy {
    @Override
    public Integer calculateAdditionalFare(List<SectionResponse> sections) {
        return sections.stream()
                .mapToInt(SectionResponse::getAdditionalFare)
                .max()
                .orElse(0);
    }
}
