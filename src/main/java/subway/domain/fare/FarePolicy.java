package subway.domain.fare;

import subway.domain.line.Line;

import java.util.Set;

public interface FarePolicy {

    int calculateFare(int distance, Set<Line> linesToUse);
}
