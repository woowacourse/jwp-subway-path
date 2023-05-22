package subway.domain.fare;

import subway.domain.Distance;

public interface FareStrategy {

    int calculteFare(Distance distance);
}
