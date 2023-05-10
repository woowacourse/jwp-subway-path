package subway.dto;

import subway.domain.Station;

public class CreationStationDto {

    private final Long id;
    private final String name;

    public CreationStationDto(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static CreationStationDto from(final Station station) {
        return new CreationStationDto(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
