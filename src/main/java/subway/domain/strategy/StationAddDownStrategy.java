package subway.domain.strategy;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.Station;
import subway.domain.graph.Graph;
import subway.exeption.InvalidDistanceException;

public class StationAddDownStrategy implements StationAddStrategy {

    @Override
    public void addToTerminal(final Graph graph,
                              final Station existingStation,
                              final Station newStation,
                              final int distance) {
        graph.addSection(existingStation, newStation, distance);
    }

    @Override
    public void addToMiddle(final Graph graph,
                            final Station existingStation,
                            final Station newStation,
                            final Station adjacentStation,
                            final int distance) {
        DefaultWeightedEdge section = graph.getSection(existingStation, adjacentStation);

        final int existingDistance = (int) graph.getSectionDistance(section);
        validateDistance(distance, existingDistance);
        final int updatedDistance = existingDistance - distance;

        graph.removeSection(existingStation, adjacentStation);
        graph.addSection(existingStation, newStation, distance);
        graph.addSection(newStation, adjacentStation, updatedDistance);
    }

    private static void validateDistance(final int distance, final int existingDistance) {
        if (existingDistance <= distance) {
            throw new InvalidDistanceException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }
    }
}
