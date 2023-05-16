package subway.line.infrastructure.persistence.entity;

import java.util.UUID;
import subway.line.domain.Station;

public class StationEntity {

    private final UUID domainId;
    private final String name;

    public StationEntity(final UUID domainId, final String name) {
        this.domainId = domainId;
        this.name = name;
    }

    public static StationEntity from(final Station station) {
        return new StationEntity(station.id(), station.name());
    }

    public Station toDomain() {
        return new Station(domainId, name);
    }

    public UUID domainId() {
        return domainId;
    }

    public String name() {
        return name;
    }
}
