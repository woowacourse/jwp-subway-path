package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Station;

public class StationsDto {
    private final List<StationDto> stations;

    private StationsDto(final List<StationDto> stations) {
        this.stations = stations;
    }

    public static StationsDto from(List<Station> stations) {
        return new StationsDto(stations.stream()
                .map(StationDto::from)
                .collect(Collectors.toList()));
    }

    public List<StationDto> getStations() {
        return stations;
    }
}
