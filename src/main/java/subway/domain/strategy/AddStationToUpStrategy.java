package subway.domain.strategy;

import subway.domain.Direction;
import subway.domain.Graph;
import subway.domain.Station;

public class AddStationToUpStrategy implements AddStationStrategy {
    @Override
    public void addToTerminal(
            final Graph graph,
            final Direction direction,
            final Station existingStation,
            final Station newStation,
            final int distance) {
        graph.addStation(newStation);
        graph.setSectionDistance(graph.addSection(newStation, existingStation), distance);
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
