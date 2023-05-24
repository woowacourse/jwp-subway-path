package subway.domain.path;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.jgrapht.GraphPath;
import subway.domain.station.Station;
import subway.exception.path.IllegalPathException;

public class Path {
    private final List<Station> stations;
    private final int totalDistance;
    private final Set<Long> passingLineIds;

    public Path(List<Station> stations, int totalDistance, Set<Long> passingLineIds) {
        this.stations = stations;
        this.totalDistance = totalDistance;
        this.passingLineIds = passingLineIds;
    }

    public static Path from(GraphPath<Station, LineEdge> graphPath) {
        validateNull(graphPath);
        List<Station> path = graphPath.getVertexList();
        int totalDistance = (int) graphPath.getWeight();
        Set<Long> passLineIds = graphPath.getEdgeList().stream()
                .map(LineEdge::getLineId)
                .collect(toSet());
        return new Path(path, totalDistance, passLineIds);
    }

    private static void validateNull(GraphPath<Station, LineEdge> path) {
        if (path == null) {
            throw new IllegalPathException("해당 경로를 찾을 수 없습니다.");
        }
    }

    public List<String> getStations() {
        return stations.stream()
                .map(Station::getName)
                .collect(toList());
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public Set<Long> getPassingLineIds() {
        return Collections.unmodifiableSet(passingLineIds);
    }
}
