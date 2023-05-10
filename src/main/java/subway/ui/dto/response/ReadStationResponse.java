package subway.ui.dto.response;

import subway.domain.Station;

public class ReadStationResponse {
    private Long id;
    private String name;

    public ReadStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ReadStationResponse of(Station station) {
        return new ReadStationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
