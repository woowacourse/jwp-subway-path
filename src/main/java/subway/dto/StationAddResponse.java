package subway.dto;

import subway.domain.Station;

public class StationAddResponse {

    private final Long id;
    private final String name;

    public StationAddResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationAddResponse from(Station savedStation) {
        return new StationAddResponse(savedStation.getId(), savedStation.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
