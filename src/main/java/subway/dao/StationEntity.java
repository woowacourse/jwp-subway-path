package subway.dao;

import subway.domain.Station;

public class StationEntity {

    private final Long id;
    private final String name;

    public StationEntity(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public StationEntity(final String name) {
        this(null, name);
    }

    public Station toStation() {
        return new Station(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
