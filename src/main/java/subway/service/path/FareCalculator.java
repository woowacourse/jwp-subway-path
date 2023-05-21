package subway.service.path;

import subway.domain.Fare;

public interface FareCalculator {
    Fare calculate(ShortestPath shortestPath);
}
