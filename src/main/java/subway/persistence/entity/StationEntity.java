package subway.persistence.entity;

import subway.domain.station.Station;
import subway.domain.station.StationName;

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

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Station mapToStation() {
        return new Station(id, new StationName(name));
    }
}
