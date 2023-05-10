package subway.dto;

import subway.entity.Station;

public class StationSaveRequest {

    private String name;

    public StationSaveRequest() {
    }

    public StationSaveRequest(final String name) {
        this.name = name;
    }

    public Station toEntity() {
        return Station.of(name);
    }

}
