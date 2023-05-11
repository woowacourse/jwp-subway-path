package subway.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class Station {
    private final Long id;
    private final String name;
    private final Map<Line, Path> ups;
    private final Map<Line, Path> downs;

    public Station(final Long id, final String name, final Map<Line, Path> ups, final Map<Line, Path> downs) {
        this.id = id;
        this.name = name;
        this.ups = ups;
        this.downs = downs;
    }

    public Station(final String name) {
        this(null, name, new HashMap<>(), new HashMap<>());
    }

    public Station(final Long id, final String name) {
        this(id, name, new HashMap<>(), new HashMap<>());
    }

    public Station(final Station station) {
        this(station.id, station.name, new HashMap<>(station.ups), new HashMap<>(station.downs));
    }

    public Station addDownStation(final Line line, final Path path) {
        final Station station = new Station(this);
        station.downs.put(line, path);

        return station;
    }

    public Station addUpStation(final Line line, final Path path) {
        final Station station = new Station(this);
        station.ups.put(line, path);

        return station;
    }

    public Optional<Path> getNextDownPath(final Line line) {
        return Optional.ofNullable(downs.get(line));
    }

    public Optional<Path> getNextUpPath(final Line line) {
        return Optional.ofNullable(ups.get(line));
    }

    public boolean nameEquals(final String name) {
        return this.name.equals(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return id.equals(station.id) && name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
