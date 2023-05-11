package subway.dto;

import subway.domain.station.Station;

public class StationResponse {
    private final Long id;
    private final String name;

    public StationResponse() {
        this(null, null);
    }

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
