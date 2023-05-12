package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static subway.domain.Direction.UP;

public class Line {
    private final Long id;
    private final String name;
    private final String color;
    private final Map<Station, Path> paths;

    public Line(final Long id, final String name, final String color, final Map<Station, Path> paths) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.paths = paths;
    }

    public Line(final String name, final String color) {
        this(null, name, color, new HashMap<>());
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, new HashMap<>());
    }

    public Line setPath(final Map<Station, Path> paths) {
        return new Line(id, name, color, paths);
    }

    public List<Station> sortStations() {
        final Station start = computeStartStation();
        final List<Station> result = new ArrayList<>();
        result.add(start);
        Station current = start;
        while (result.size() != paths.size() + 1) {
            current = paths.get(current).getNext();
            result.add(current);
        }
        return result;
    }

    private Station computeStartStation() {
        final List<Station> ups = new ArrayList<>(paths.keySet());
        final List<Station> downs = paths.values().stream()
                .map(Path::getNext)
                .collect(Collectors.toList());
        ups.removeAll(downs);
        return ups.get(0);
    }

    public void addPath(
            final Station targetStation,
            final Station addStation,
            final Integer distance,
            final Direction direction
    ) {
        final List<Station> stations = sortStations();
        final int index = stations.indexOf(targetStation);
        if (direction == UP) {
            if (paths.isEmpty()) {
                paths.put(addStation, new Path(targetStation, distance));
            }
            if (index == 0) {
                paths.put(addStation, new Path(targetStation, distance));
                return;
            }
            final Station stationBefore = stations.get(index - 1);
            final Path path = paths.get(stationBefore);
            validatePathDistance(distance, path);
            paths.put(stationBefore, new Path(addStation, path.getDistance() - distance));
            paths.put(addStation, new Path(targetStation, distance));
        }

        if (paths.isEmpty()) {
            paths.put(targetStation, new Path(addStation, distance));
        }
        if (index == stations.size() - 1) {
            paths.put(targetStation, new Path(addStation, distance));
            return;
        }
        final Path path = paths.get(targetStation);
        validatePathDistance(distance, path);
        paths.put(targetStation, new Path(addStation, distance));
        paths.put(addStation, new Path(path.getNext(), path.getDistance() - distance));

    }

    private void validatePathDistance(final Integer distance, final Path path) {
        if (path.isShorterThan(distance)) {
            throw new IllegalArgumentException("기존 경로보다 짧은 경로를 추가해야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Map<Station, Path> getPaths() {
        return paths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(id, line.id) && Objects.equals(name, line.name) && Objects.equals(color, line.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color);
    }
}
