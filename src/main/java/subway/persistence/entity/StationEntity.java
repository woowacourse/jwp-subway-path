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
        return new StationEntity(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
