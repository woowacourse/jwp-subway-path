package subway.domain.path;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.station.Station;
import subway.exception.path.IllegalPathException;

public class Path {
    private final GraphPath<Station, DefaultWeightedEdge> path;

    public Path(GraphPath<Station, DefaultWeightedEdge> path) {
        validateNull(path);
        this.path = path;
    }

    private void validateNull(GraphPath<Station, DefaultWeightedEdge> path) {
        if (path == null) {
            throw new IllegalPathException("해당 경로를 찾을 수 없습니다.");
        }
    }

    public List<String> getStations() {
        return path.getVertexList().stream()
                .map(Station::getName)
                .collect(toList());
    }

    public int getTotalDistance() {
        return (int) path.getWeight();
    }
}
