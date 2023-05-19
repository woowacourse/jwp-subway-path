package subway.controller.dto.response;

import subway.domain.Station;
import subway.entity.StationEntity;

public class StationResponse {

    private final long id;
    private final String name;

    public StationResponse(final long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse createByDomain(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static StationResponse createByEntity(final StationEntity entity) {
        return new StationResponse(entity.getId(), entity.getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
