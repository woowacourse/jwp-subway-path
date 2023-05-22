package subway.domain.station;

import java.util.Objects;

public class Station {

    private Long id;
    private Name name;

    public Station() {
    }

    public Station(final Long id, final String name) {
        this.id = id;
        this.name = new Name(name);
    }

    public Station(final String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
