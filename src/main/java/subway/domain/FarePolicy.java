package subway.domain;

import java.util.List;
import subway.domain.section.Section;

public interface FarePolicy {

    Fare calculate(List<Section> sections);
}
