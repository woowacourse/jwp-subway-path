package subway.domain.path;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.domain.line.Line;
import subway.domain.section.Sections;
import subway.domain.station.Station;

public class Path extends WeightedMultigraph<Station, PathSection> {

    private static final int FIRST_PATH_SECTION_INDEX = 0;
    private static final int START_PATH_SECTION_INDEX = 1;

    private Path(final Class<? extends PathSection> edgeClass) {
        super(edgeClass);
    }

    public static Path from(List<Line> lines) {
        final Path path = new Path(PathSection.class);

        for (Line line : lines) {
            path.addSectionPath(line);
        }

        return path;
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

            final PathSection pathSection = PathSection.of(station, adjustStation, line);

            addEdge(station, adjustStation, pathSection);
            setEdgeWeight(pathSection, pathSection.getWeight());
        }
    }

    public List<PathSections> findShortestPathSections(final Station sourceStation, final Station targetStation) {
        final List<PathSection> shortestPathSections = calculateShortestPathEdge(sourceStation, targetStation);
        final List<PathSections> result = new ArrayList<>();

        PathSections pathSections = PathSections.create();
        pathSections.add(shortestPathSections.get(FIRST_PATH_SECTION_INDEX));

        for (int i = START_PATH_SECTION_INDEX; i < shortestPathSections.size(); i++) {
            if (pathSections.isOtherLine(shortestPathSections.get(i))) {
                result.add(pathSections);
                pathSections = PathSections.create();
            }
            pathSections.add(shortestPathSections.get(i));
        }
        result.add(pathSections);

        return result;
    }

    private List<PathSection> calculateShortestPathEdge(final Station sourceStation, final Station targetStation) {
        final DijkstraShortestPath<Station, PathSection> dijkstraShortestPath = new DijkstraShortestPath<>(this);

        return dijkstraShortestPath.getPath(sourceStation, targetStation).getEdgeList();
    }
}
