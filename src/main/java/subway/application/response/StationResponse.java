package subway.application.response;

import subway.domain.station.Station;

public class StationResponse {
    private final Long id;
    private final String name;

    public StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public StationResponse(final Station station) {
        this(station.getId(), station.getName().getValue());
    }

    private StationResponse() {
        this(null, null);
    }

    public static StationResponse of(final Station station) {
        return new StationResponse(station.getId(), station.getName().getValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
