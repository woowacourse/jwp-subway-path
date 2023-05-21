package subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Station {
    private final Long id;
    private final Name name;

    public Station(final String name) {
        this(null, name);
    }

    public Station(final Long id, String name) {
        this.id = id;
        this.name = new Name(name);
    }

    public static Station of(Long id, String name) {
        return new Station(id, name);
    }

    public static Station from(String name) {
        return new Station(null, name);
    }

    public Station(final Long id, Name name) {
        this.id = id;
        this.name = name;
    }

    public boolean equalsName(Station station) {
        return this.name.equals(station.name);
    }
}
