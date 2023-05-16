package subway.dao.entity;

import subway.domain.Station;

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
}
