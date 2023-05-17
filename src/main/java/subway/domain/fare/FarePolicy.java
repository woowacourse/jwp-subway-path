package subway.domain.fare;

import java.util.List;
import subway.domain.section.Section;

public interface FarePolicy {

    Fare calculate(List<Section> sections);
}
