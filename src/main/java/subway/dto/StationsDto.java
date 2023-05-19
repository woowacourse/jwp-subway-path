package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.Station;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class StationsDto {
    private final List<StationDto> stations;

    public static StationsDto from(List<Station> stations) {
        return new StationsDto(stations.stream()
                .map(StationDto::from)
                .collect(Collectors.toList()));
    }
}
