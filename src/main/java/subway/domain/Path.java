package subway.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Path {

    private final List<Station> path;
    private final int distance;

    public Path(List<Station> path, int distance) {
        this.path = path;
        this.distance = distance;
    }

    public List<String> getPath() {
        return path.stream()
                   .map(Station::getName)
                   .collect(Collectors.toList());
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path1 = (Path) o;
        return distance == path1.distance && Objects.equals(path, path1.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, distance);
    }

    @Override
    public String toString() {
        return "Path{" +
                "path=" + path +
                ", distance=" + distance +
                '}';
    }
}
