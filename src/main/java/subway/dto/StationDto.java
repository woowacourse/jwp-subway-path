package subway.dto;

import java.util.Objects;
import subway.domain.Station;

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

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationDto that = (StationDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
