package subway.line.domain;

import java.util.Objects;
import java.util.UUID;

public class Station {

    private final UUID id;
    private final String name;

    public Station(final String name) {
        this(UUID.randomUUID(), name);
    }

    public Station(final UUID id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Station)) {
            return false;
        }
        final Station station = (Station) o;
        return Objects.equals(id, station.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }
}
