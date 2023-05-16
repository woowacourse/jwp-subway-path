package subway.domain;

import subway.domain.dijkstra.Dijkstra;

import java.util.List;
import java.util.stream.Collectors;

public class ShortestWayCalculator extends Dijkstra {

    private final Integer distance;
    private final List<Station> way;

    public ShortestWayCalculator(final Integer distance, final List<Station> way) {
        this.distance = distance;
        this.way = way;
    }

    public ShortestWayCalculator() {
        this(null, null);
    }

    public ShortestWayCalculator calculate(final Station start, final Station end, final List<Line> lines) {
        final List<Station> stations = integrateStations(lines);

        final var result = shortestPath(stations, lines, start, end);

        try {
            return new ShortestWayCalculator(((int) result.getWeight()), result.getVertexList());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("갈 수 없는 경로입니다.");
        }
    }

    private List<Station> integrateStations(final List<Line> lines) {
        return lines.stream()
                .map(Line::sortStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Integer getDistance() {
        return distance;
    }

    public List<Station> getWay() {
        return way;
    }
}
