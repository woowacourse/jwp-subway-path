package subway.dto;

import subway.domain.station.Station;

public class StationSaveDto {

    private final Long id;
    private final String name;

    private StationSaveDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationSaveDto from(Station station) {
        return new StationSaveDto(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "StationDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
