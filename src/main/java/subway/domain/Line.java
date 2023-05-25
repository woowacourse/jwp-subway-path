package subway.domain;

import subway.domain.addpathstrategy.AddPathStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        if (paths.isEmpty()) {
            return List.of();
        }
        final Station start = computeStartStation();
        final List<Station> result = new ArrayList<>();
        result.add(start);
        Station current = start;
        while (sortingNotCompleted(result)) {
            current = paths.get(current).getNext();
            result.add(current);
        }
        return result;
    }

    private boolean sortingNotCompleted(final List<Station> result) {
        return result.size() != paths.size() + 1;
    }

    private Station computeStartStation() {
        final List<Station> ups = new ArrayList<>(paths.keySet());
        final List<Station> downs = paths.values().stream()
                .map(Path::getNext)
                .collect(Collectors.toList());
        ups.removeAll(downs);
        return ups.get(0);
    }

    public Line addPath(
            final Station targetStation,
            final Station addStation,
            final Integer distance,
            final Direction direction
    ) {
        final List<Station> stations = sortStations();
        final AddPathStrategy strategy = direction.getStrategy();
        final Map<Station, Path> added = strategy.add(targetStation, addStation, distance, stations, paths);

        return setPath(added);
    }

    public void removeStation(final Station station) {
        final List<Station> stations = sortStations();
        final int index = stations.indexOf(station);
        if (paths.size() == 1) {
            paths.clear();
            return;
        }
        if (index == 0) {
            paths.remove(station);
            return;
        }
        if (isEndOfTheLine(stations, index)) {
            paths.remove(stations.get(index - 1));
            return;
        }
        if (index > 0) {
            final Station stationBefore = stations.get(index - 1);
            final Path pathBefore = paths.get(stationBefore);
            final Path path = paths.get(station);
            final int distance = path.sumDistance(pathBefore);
            paths.remove(station);
            paths.put(stationBefore, new Path(path.getNext(), distance));
        }
    }

    private boolean isEndOfTheLine(final List<Station> stations, final int index) {
        return index == stations.size() - 1 && !stations.isEmpty();
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
        return new HashMap<>(paths);
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
