package subway.entity;

import subway.domain.Station;

public class StationEntity {
    private final Long id;
    private final String name;

    private StationEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationEntity from(final Station station) {
        return new StationEntity(station.getId(), station.getName());
    }

    public static StationEntity of(final long id, final String name) {
        return new StationEntity(id, name);
    }

    public Station toDomain() {
        return new Station(id, this.name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
