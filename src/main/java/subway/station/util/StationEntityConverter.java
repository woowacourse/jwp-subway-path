package subway.station.util;

import java.util.List;
import java.util.stream.Collectors;
import subway.station.dto.StationResponseDto;
import subway.station.persistence.StationEntity;

public class StationEntityConverter {

    private StationEntityConverter() {

    }

    public static StationResponseDto convertToStationResponseDto(final StationEntity stationEntity) {
        return new StationResponseDto(stationEntity.getId(), stationEntity.getName());
    }

    public static List<StationResponseDto> convertToStationResponseDto(final List<StationEntity> stationEntity) {
        return stationEntity.stream()
            .map(StationEntityConverter::convertToStationResponseDto)
            .collect(Collectors.toList());
    }
}
