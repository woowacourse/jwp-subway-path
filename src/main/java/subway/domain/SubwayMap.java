package subway.domain;

import subway.domain.graph.SubwayGraph;

import java.util.List;

public class SubwayMap {

    private final Lines lines;
    private final SubwayGraph subwayGraph;

    public SubwayMap(final Lines lines, final SubwayGraph subwayGraph) {
        this.lines = lines;
        this.subwayGraph = subwayGraph;
    }

    public List<Section> calculateShortestPaths(final Station from, final Station to) {
        List<Station> vertexes = lines.getAllStations();
        List<Section> edges = lines.getAllSections();
        subwayGraph.addVertexes(vertexes);
        subwayGraph.addEdges(edges);

        return  subwayGraph.getDijkstraShortestPath(from, to);
    }

    public Integer calculateTotalDistance(final List<Section> sections) {
        return sections.stream()
                .map(section -> section.getDistance().getValue())
                .reduce(0, Integer::sum);
    }
}
