package subway.dto.station;

import subway.entity.StationEntity;

public class StationSelectResponse {
    private final Long id;
    private final String name;

    public StationSelectResponse(final String name) {
        this(null, name);
    }

    public StationSelectResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationSelectResponse from(StationEntity station) {
        return new StationSelectResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
