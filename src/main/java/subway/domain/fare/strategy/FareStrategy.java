package subway.domain.fare.strategy;

import subway.domain.fare.FareInfo;

public interface FareStrategy {
    FareInfo calculate(FareInfo fareInfo);
}
