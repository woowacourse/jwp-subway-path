package subway.ui.dto;

import subway.service.dto.StationDto;

public class StationResponse {

    private final Long id;
    private final String name;

    public StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(final StationDto stationDto) {
        return new StationResponse(stationDto.getId(), stationDto.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
