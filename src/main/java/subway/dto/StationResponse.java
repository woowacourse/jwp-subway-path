package subway.dto;

import subway.domain.Station;

public class StationResponse {

    private final Long id;
    private final String name;

    public StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    private StationResponse() {
        this(null, null);
    }

    public static StationResponse of(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
