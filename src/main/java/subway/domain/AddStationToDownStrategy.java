package subway.domain;

import subway.domain.strategy.AddStationStrategy;

public class AddStationToDownStrategy implements AddStationStrategy {

    @Override
    public void addToTerminal(final Graph graph,
                              final Direction direction,
                              final Station existingStation,
                              final Station newStation,
                              final int distance) {
        graph.addStation(newStation);
        graph.setSectionDistance(graph.addSection(existingStation, newStation), distance);
    }

    @Override
    public void addToMiddle(final Graph graph,
                            final Direction direction,
                            final Station existingStation,
                            final Station newStation,
                            final Station adjacentStation,
                            final int distance) {

    }
}
