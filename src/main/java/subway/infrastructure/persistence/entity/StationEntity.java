package subway.infrastructure.persistence.entity;

import subway.domain.Station;

public class StationEntity {

    private final String name;

    public StationEntity(final String name) {
        this.name = name;
    }

    public static StationEntity from(final Station station) {
        return new StationEntity(station.getName());
    }

    public Station toDomain() {
        return new Station(name);
    }

    public String getName() {
        return name;
    }
}
