package subway.domain.strategy;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.graph.Graph;
import subway.domain.Station;
import subway.exeption.InvalidDistanceException;

public class AddStationToDownStrategy implements AddStationStrategy {

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
        System.out.println("existingDistance = " + existingDistance);
        System.out.println("distance = " + distance);

        if (existingDistance <= distance) {
            throw new InvalidDistanceException("새로운 역의 거리는 기존 두 역의 거리보다 작아야 합니다.");
        }

        final int updatedDistance = existingDistance - distance;

        graph.removeSection(existingStation, adjacentStation);
        graph.addSection(existingStation, newStation, distance);
        graph.addSection(newStation, adjacentStation, updatedDistance);
    }
}
