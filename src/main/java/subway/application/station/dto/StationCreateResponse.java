package subway.application.station.dto;

import subway.domain.Station;

public class StationCreateResponse {

    private final long id;
    private final String name;

    public StationCreateResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationCreateResponse of(Station station) {
        return new StationCreateResponse(station.getId(), station.getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
