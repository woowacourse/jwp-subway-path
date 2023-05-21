package subway.ui.dto.station;

import subway.domain.Station;

public class StationResponse {
    private long id;
    private String name;

    public StationResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
