package subway.dto.station;

import subway.domain.Station;

public class StationResponse {

    private final Long id;
    private final String name;

    private StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(final Long id, final Station station) {
        return new StationResponse(id, station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
