package subway.domain;

import subway.domain.strategy.AddStationStrategy;
import subway.domain.strategy.AddStationToUpStrategy;

public enum Direction {
    UP(new AddStationToUpStrategy()),
    DOWN(new AddStationToDownStrategy());

    private final AddStationStrategy strategy;

    Direction(final AddStationStrategy strategy) {
        this.strategy = strategy;
    }

    public void addStationToTerminal(
            final Graph graph,
            final Station existingStation,
            final Station newStation,
            final int distance) {
        strategy.addToTerminal(graph, this, existingStation, newStation, distance);
    }

    public void addStationToMiddle(
            final Graph graph,
            final Station existingStation,
            final Station newStation,
            final Station adjacentStation,
            final int distance) {
        strategy.addToTerminal(graph, this, existingStation, adjacentStation, distance);
    }
}
