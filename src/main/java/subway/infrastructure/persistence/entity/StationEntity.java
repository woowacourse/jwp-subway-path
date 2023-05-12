package subway.infrastructure.persistence.entity;

import java.util.UUID;
import subway.domain.Station;

public class StationEntity {

    private final Long id;
    private final UUID domainId;
    private final String name;

    public StationEntity(final UUID domainId, final String name) {
        this(null, domainId, name);
    }

    public StationEntity(final Long id, final UUID domainId, final String name) {
        this.id = id;
        this.domainId = domainId;
        this.name = name;
    }

    public static StationEntity from(final Station station) {
        return new StationEntity(station.id(), station.name());
    }

    public Station toDomain() {
        return new Station(domainId, name);
    }

    public Long id() {
        return id;
    }

    public UUID domainId() {
        return domainId;
    }

    public String name() {
        return name;
    }
}
