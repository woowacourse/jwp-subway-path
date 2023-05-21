package subway.domain.fare;

import subway.domain.line.Line;
import subway.domain.line.Lines;

import java.util.List;

public interface FarePolicy {

    int calculateFare(int distance, Lines lines);
}
