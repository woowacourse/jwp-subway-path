package subway.domain.fare;

import java.util.List;
import subway.domain.routestrategy.SubwaySection;

public interface FareStrategy {

    int calculateFare(List<SubwaySection> distance);
}
