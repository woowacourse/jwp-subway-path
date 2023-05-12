package subway.business.converter;

import subway.business.domain.Station;
import subway.persistence.entity.StationEntity;
import subway.presentation.dto.response.StationResponse;

public class StationEntityDomainConverter {

    public static StationResponse domainToResponseDto(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static Station entityToDomain(StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

}
