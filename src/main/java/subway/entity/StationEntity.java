package subway.entity;

import java.util.Objects;
import subway.domain.Name;
import subway.domain.station.Station;

public class StationEntity {
    private final Long id;
    private final String name;

    private StationEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationEntity of(long id, Station station) {
        return new StationEntity(id, station.getName().getName());
    }
    public static StationEntity of(long id, Name name) {
        return new StationEntity(id, name.getName());
    }

    public static StationEntity of(long id, String name) {
        return new StationEntity(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationEntity that = (StationEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
