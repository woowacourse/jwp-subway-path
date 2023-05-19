package subway.domain;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import subway.exeption.InvalidPathException;
import subway.exeption.LineNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Subway {
    private final List<Sections> sections;

    public Subway(final List<Sections> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Map<Line, List<Station>> findAll() {
        return sections.stream()
                .collect(Collectors.toMap(
                        Sections::getLine,
                        Sections::findAllStationsInOrder));
    }

    public List<Line> findAllLines() {
        return sections.stream()
                .map(Sections::getLine)
                .collect(Collectors.toList());
    }

    public void createSectionsOf(final Line line, final Graph graph) {
        final Sections newSections = new Sections(line, graph);
        sections.add(newSections);
    }

    public void addSection(final Line line, final Section section) {
        final Sections newSections = findSectionsOf(line);
        newSections.createInitialSection(section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public Station addStation(final Line line, final Station upStation, final Station downStation, final int distance) {
        final Sections sections = findSectionsOf(line);
        return sections.addStation(upStation, downStation, distance);
    }

    public Sections findSectionsOf(final Line line) {
        return sections.stream().filter(element -> element.isSameLine(line))
                .findFirst()
                .orElseThrow(() -> new LineNotFoundException("해당 노선이 존재하지 않습니다."));
    }

    public List<Station> findStationsInOrder(final Line line) {
        final Sections sections = findSectionsOf(line);
        return sections.findAllStationsInOrder();
    }

    public Station findStationBefore(final Line line, final Station station) {
        final Sections sections = findSectionsOf(line);
        return sections
                .findAdjacentStationOf(station, element -> sections.getUpStationsOf(station));
    }

    public Station findStationAfter(final Line line, final Station station) {
        final Sections sections = findSectionsOf(line);
        return sections
                .findAdjacentStationOf(station, element -> sections.getDownStationsOf(station));
    }

    public WeightedMultigraph<Station, DefaultWeightedEdge> findAllStationGraph() {
        final List<Graph> graphs = sections.stream()
                .map(Sections::getGraph)
                .collect(Collectors.toList());

        WeightedMultigraph<Station, DefaultWeightedEdge> mergedGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (final Graph graph : graphs) {
            Graphs.addGraph(mergedGraph, graph.getGraph());
        }

        return mergedGraph;
    }

    public PathDto findShortestPath(final Station source, final Station target) {
        final WeightedMultigraph<Station, DefaultWeightedEdge> allStationGraph = findAllStationGraph();

        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(allStationGraph);

        final GraphPath<Station, DefaultWeightedEdge> path = shortestPath.getPath(source, target);

        if (path == null) {
            throw new InvalidPathException("연결되지 않은 역에 대해 경로를 조회할 수 없습니다.");
        }

        final List<Station> stations = path.getVertexList();
        double distance = shortestPath.getPathWeight(source, target);

        return new PathDto(stations, distance);
    }
}
