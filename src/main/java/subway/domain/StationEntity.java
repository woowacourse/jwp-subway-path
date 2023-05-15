package subway.domain;

import java.util.Objects;

public class StationEntity {

    private final Long id;
    private final String name;

    private StationEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationEntity of(final String name) {
        return new StationEntity(null, name);
    }

    public static StationEntity of(final Long id, final String name) {
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
        final StationEntity stationEntity = (StationEntity) o;
        return Objects.equals(id, stationEntity.id) && Objects.equals(name, stationEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
