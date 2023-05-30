package subway.domain.path;

import subway.domain.line.Line;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class Path {

    private final List<String> stationNames;
    private final List<SectionEdge> sectionEdges;
    private final int distance;

    public Path(List<String> stationNames, List<SectionEdge> sectionEdges, int distance) {
        this.stationNames = stationNames;
        this.sectionEdges = sectionEdges;
        this.distance = distance;
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public int getDistance() {
        return distance;
    }

    public Set<Line> getLinesToUse() {
        return sectionEdges.stream()
                .map(SectionEdge::getLine)
                .collect(toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return distance == path.distance && Objects.equals(stationNames, path.stationNames) && Objects.equals(sectionEdges, path.sectionEdges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stationNames, sectionEdges, distance);
    }

    @Override
    public String toString() {
        return "Path{" +
                "stationNames=" + stationNames +
                ", sectionEdges=" + sectionEdges +
                ", distance=" + distance +
                '}';
    }
}
