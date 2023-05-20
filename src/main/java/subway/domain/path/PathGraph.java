package subway.domain.path;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class PathGraph extends WeightedMultigraph<Station, PathEdge> {

    private static final int FIRST_PATH_SECTION_INDEX = 0;
    private static final int START_PATH_SECTION_INDEX = 1;

    private PathGraph(final Class<? extends PathEdge> edgeClass) {
        super(edgeClass);
    }

    public static PathGraph from(List<Line> lines) {
        final PathGraph pathGraph = new PathGraph(PathEdge.class);

        for (Line line : lines) {
            pathGraph.addSectionPath(line);
        }

        return pathGraph;
    }

    private void addSectionPath(final Line line) {
        final Sections sections = line.getSections();
        final List<Station> sectionStations = sections.findStationsByOrdered();

        for (Station station : sectionStations) {
            addPath(line, sections.findAllAdjustStationByStation(station), station);
        }
    }

    private void addPath(final Line line, final List<Station> adjustStations, final Station station) {
        addVertex(station);

        for (Station adjustStation : adjustStations) {
            addVertex(adjustStation);

            final PathEdge pathEdge = PathEdge.of(station, adjustStation, line);

            addEdge(station, adjustStation, pathEdge);
            setEdgeWeight(pathEdge, pathEdge.getWeight());
        }
    }

    public List<PathEdges> findShortestPathSections(final Station sourceStation, final Station targetStation) {
        final List<PathEdge> shortestPathEdges = calculateShortestPathEdge(sourceStation, targetStation);
        final List<PathEdges> result = new ArrayList<>();

        PathEdges pathEdges = PathEdges.create();
        pathEdges.add(shortestPathEdges.get(FIRST_PATH_SECTION_INDEX));

        for (int i = START_PATH_SECTION_INDEX; i < shortestPathEdges.size(); i++) {
            if (pathEdges.isOtherLine(shortestPathEdges.get(i))) {
                result.add(pathEdges);
                pathEdges = PathEdges.create();
            }
            pathEdges.add(shortestPathEdges.get(i));
        }
        result.add(pathEdges);

        return result;
    }

    private List<PathEdge> calculateShortestPathEdge(final Station sourceStation, final Station targetStation) {
        final DijkstraShortestPath<Station, PathEdge> dijkstraShortestPath = new DijkstraShortestPath<>(this);

        return dijkstraShortestPath.getPath(sourceStation, targetStation).getEdgeList();
    }
}
