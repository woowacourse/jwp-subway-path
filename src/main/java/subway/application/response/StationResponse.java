package subway.application.response;

import subway.domain.Station;

public class StationResponse {
    private final Long id;
    private final String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(final Station station) {
        return new StationResponse(station.getId(), station.getNameValue());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
