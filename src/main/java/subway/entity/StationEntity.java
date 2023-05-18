package subway.entity;

import subway.domain.station.Station;

public class StationEntity implements Entity{

    private final Long id;
    private final String name;

    public StationEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationEntity from(final Station station) {
        return new StationEntity(station.getId(), station.getName());
    }

    public Station toDomain() {
        return new Station(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
