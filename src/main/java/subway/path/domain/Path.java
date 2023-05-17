package subway.path.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.line.domain.Line;
import subway.section.domain.Section;
import subway.station.domain.Station;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class Path {
    private static final int BASE_FEE = 1250;
    private static final int BASE_DISTANCE = 10;
    private static final int RANGE_OF_FIVE_INCREASE = 40;
    private static final int START_DISTANCE_OF_EIGHT_INCREASE = BASE_DISTANCE + RANGE_OF_FIVE_INCREASE;
    private static final int FIVE_INCREASE = 5;
    private static final int EIGHT_INCREASE = 8;
    
    private final Set<Line> lines;
    
    public Path() {
        this(new HashSet<>());
    }
    
    public ShortestPathResult getShortestPath(final String startStationName, final String endStationName) {
        final WeightedMultigraph<Station, Section> graph = new WeightedMultigraph<>(Section.class);
        final DijkstraShortestPath<Station, Section> path = new DijkstraShortestPath<>(graph);
        lines.forEach(line -> line.addLineToGraph(graph));
        final GraphPath<Station, Section> graphPath = path.getPath(new Station(startStationName), new Station(endStationName));
        
        final List<String> shortestPath = getShortestPath(graphPath);
        final Long shortestDistance = (long) graphPath.getWeight();
        return new ShortestPathResult(shortestPath, shortestDistance, calculateFee(shortestDistance));
    }
    
    private List<String> getShortestPath(final GraphPath<Station, Section> graphPath) {
        return graphPath.getVertexList().stream()
                .map(Station::getName)
                .collect(Collectors.toUnmodifiableList());
    }
    
    private long calculateFee(final Long distance) {
        return BASE_FEE
                + calculateWithSpecifiedIncrement(Math.min(RANGE_OF_FIVE_INCREASE, distance - BASE_DISTANCE), FIVE_INCREASE)
                + calculateWithSpecifiedIncrement(distance - START_DISTANCE_OF_EIGHT_INCREASE, EIGHT_INCREASE);
    }
    
    private long calculateWithSpecifiedIncrement(final long distance, final int increase) {
        if (isDistanceNegative(distance)) {
            return 0;
        }
        
        return (long) ((Math.ceil((distance - 1) / increase) + 1) * 100);
    }
    
    private boolean isDistanceNegative(final long distance) {
        return distance <= 0;
    }
}
