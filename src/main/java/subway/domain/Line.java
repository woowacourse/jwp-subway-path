package subway.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

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
        final Set<Station> ups = new HashSet<>(paths.keySet());
        final Set<Station> downs = paths.values().stream()
                .map(Path::getNext)
                .collect(toSet());
        ups.removeAll(downs);
        return ups.stream().findAny().get();
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
