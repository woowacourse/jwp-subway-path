package subway.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.exception.custom.LineDoesNotContainStationException;

public class SubwayMap {

    private final Map<Line, Sections> linesAndSections;

    public SubwayMap(final List<Line> lines, final List<Sections> linePaths) {
        if (lines == null || linePaths == null || lines.size() != linePaths.size()) {
            throw new IllegalArgumentException("지하철 노선도를 생성하기에 부적절한 정보입니다.");
        }
        this.linesAndSections = IntStream.range(0, lines.size())
            .boxed()
            .collect(Collectors.toMap(lines::get, linePaths::get));
    }

    public ShortestPath getShortestPath(final Station start, final Station end) {
        final WeightedMultigraph<LineStation, DefaultEdge> graph = generateGraph();
        final DijkstraShortestPath<LineStation, DefaultEdge> shortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<LineStation, DefaultEdge> path = shortestPath.getPath(LineStation.withNullLine(start),
            LineStation.withNullLine(end));

        return new ShortestPath(path.getVertexList(), (int) path.getWeight());
    }

    private WeightedMultigraph<LineStation, DefaultEdge> generateGraph() {
        final WeightedMultigraph<LineStation, DefaultEdge> graph = new WeightedMultigraph<>(DefaultEdge.class);

        linesAndSections.forEach((line, sections) -> {
            addSectionStationsToVertex(graph, line, sections.getStations());
            addSectionsToEdge(graph, line, sections.getSections());
        });

        return graph;
    }

    private void addSectionStationsToVertex(final WeightedMultigraph<LineStation, DefaultEdge> graph, final Line line,
        final List<Station> stations) {
        stations.forEach((station) -> graph.addVertex(LineStation.of(line, station)));
    }

    private void addSectionsToEdge(final WeightedMultigraph<LineStation, DefaultEdge> graph, final Line line,
        final List<Section> sections) {
        sections.forEach(section -> {
            final LineStation upStation = LineStation.of(line, section.getUpStation());
            final LineStation downStation = LineStation.of(line, section.getDownStation());
            graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
        });
    }

    public Line findLineContains(final Station station) {
        return linesAndSections.entrySet()
            .stream()
            .filter((entry) -> entry.getValue().getStations().contains(station))
            .findFirst()
            .orElseThrow(() -> new LineDoesNotContainStationException(String.format("%s 역을 포함한 라인이 존재하지 않습니다.",
                station.getName()))).getKey();
    }
}
