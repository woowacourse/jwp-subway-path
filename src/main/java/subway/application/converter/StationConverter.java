package subway.application.converter;

import subway.application.domain.Line;
import subway.application.domain.Station;
import subway.entity.StationEntity;
import subway.ui.dto.response.StationResponse;

public class StationConverter {

    public static StationResponse domainToResponseDto(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static Station entityToDomain(StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

}
