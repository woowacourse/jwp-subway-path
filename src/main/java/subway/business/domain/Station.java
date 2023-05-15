package subway.business.domain;

import java.util.Objects;

public class Station {
    private Name name;

    public Station(Name name) {
        this.name = name;
    }

    public Station(String name) {
        this(new Name(name));
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
