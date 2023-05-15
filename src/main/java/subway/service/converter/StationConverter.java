package subway.service.converter;

import subway.service.domain.Station;
import subway.entity.StationEntity;
import subway.controller.dto.response.StationResponse;

public class StationConverter {

    public static StationResponse domainToResponseDto(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static Station entityToDomain(StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

}
