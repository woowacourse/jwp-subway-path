package subway.application.dto;

import subway.domain.station.Station;

public class ReadStationDto {

    private final Long id;
    private final String name;

    private ReadStationDto(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static ReadStationDto from(final Station station) {
        return new ReadStationDto(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
