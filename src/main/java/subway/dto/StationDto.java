package subway.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import subway.domain.Station;

@Getter
@EqualsAndHashCode
public class StationDto {
    private Long id;
    private final String name;

    private StationDto(final String name) {
        this.name = name;
    }

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
