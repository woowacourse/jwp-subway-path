package subway.ui.dto.response;

import subway.domain.Station;

public class ReadStationResponse {

    private final Long id;
    private final String name;

    public ReadStationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static ReadStationResponse from(final Station station) {
        return new ReadStationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
