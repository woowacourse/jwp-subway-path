package subway.domain;

import subway.domain.graph.Graph;
import subway.domain.strategy.StationAddStrategy;
import subway.domain.strategy.StationAddDownStrategy;
import subway.domain.strategy.StationAddUpStrategy;

public enum Direction {
    UP(new StationAddUpStrategy()),
    DOWN(new StationAddDownStrategy());

    private final StationAddStrategy strategy;

    Direction(final StationAddStrategy strategy) {
        this.strategy = strategy;
    }

    public void addStationToTerminal(
            final Graph graph,
            final Station existingStation,
            final Station newStation,
            final int distance) {
        strategy.addToTerminal(graph, existingStation, newStation, distance);
    }

    public void addStationToMiddle(
            final Graph graph,
            final Station existingStation,
            final Station newStation,
            final Station adjacentStation,
            final int distance) {
        strategy.addToMiddle(graph, existingStation, newStation, adjacentStation, distance);
    }
}
