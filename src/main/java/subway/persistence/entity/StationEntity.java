package subway.persistence.entity;

import subway.domain.Station;

public class StationEntity {

    private final Long id;
    private final String name;

    public StationEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationEntity from(final Station station) {
        return new StationEntity(null, station.getName());
    }

    public static StationEntity from(final String name) {
        return new StationEntity(null, name);
    }

    public static StationEntity of(final Long id, final String name) {
        return new StationEntity(id, name);
    }

    public Station toDomain() {
        return Station.of(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
