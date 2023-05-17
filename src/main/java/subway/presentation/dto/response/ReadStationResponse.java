package subway.presentation.dto.response;

import subway.application.dto.ReadStationDto;

public class ReadStationResponse {

    private final Long id;
    private final String name;

    private ReadStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ReadStationResponse from(final ReadStationDto dto) {
        return new ReadStationResponse(dto.getId(), dto.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
