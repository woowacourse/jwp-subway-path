package subway.service;

import subway.domain.Fare;

public interface FareCalculator {
    Fare calculate(ShortestPath shortestPath);
}
