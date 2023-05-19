package subway.domain.farecalculator;

import java.util.List;

import subway.dto.FareResponse;
import subway.dto.SectionResponse;

public interface FareCalculator {
    FareResponse calculate(List<SectionResponse> sections, Integer distance, Integer age);
}
