package subway.dao.entity;

import subway.domain.Station;

import java.util.Objects;

public class StationEntity {

    private final Long id;
    private final String name;

    public StationEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationEntity from(final Station station) {
        return new StationEntity(
                station.getId(),
                station.getName()
        );
    }

    public Station convertToStation() {
        return new Station(this.id, this.name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StationEntity that = (StationEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StationEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
