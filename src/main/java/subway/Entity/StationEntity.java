package subway.Entity;

import subway.domain.station.Station;

import java.util.Objects;

public class StationEntity {

    private final Long id;
    private final String name;

    public StationEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Station mapToStation() {
        return Station.from(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationEntity other = (StationEntity) o;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
