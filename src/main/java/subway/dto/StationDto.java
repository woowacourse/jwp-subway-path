package subway.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import subway.domain.Station;

@Getter
@EqualsAndHashCode
public class StationDto {
    private final Long id;
    private final String name;

    public StationDto(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationDto from(Station station) {
        return new StationDto(station.getId(), station.getName().getName());
    }

    public static StationDto of(Long id, String name) {
        return new StationDto(id, name);
    }

    public static StationDto of(Long id, Station station) {
        return new StationDto(id, station.getName().getName());
    }
}
