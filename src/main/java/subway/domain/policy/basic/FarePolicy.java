package subway.domain.policy.basic;

import subway.domain.line.Lines;

public interface FarePolicy {

    int calculateFare(int distance, Lines lines);
}
