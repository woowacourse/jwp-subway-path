package subway.dto;

import subway.entity.StationEntity;

public class StationResponse {
    private final Long id;
    private final String name;

    public StationResponse(final String name) {
        this(null, name);
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(StationEntity station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
