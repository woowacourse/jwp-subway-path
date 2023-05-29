package subway.application.service.mapper;

import subway.adapter.out.persistence.entity.StationEntity;
import subway.application.port.in.station.dto.response.StationQueryResponse;
import subway.domain.station.Station;

public class StationMapper {

    private StationMapper() {
    }

    public static Station toStation(final StationEntity entity) {
        return new Station(entity.getId(), entity.getName());
    }

    public static StationQueryResponse toResponse(final Station station) {
        return new StationQueryResponse(station.getId(), station.getName());
    }

    public static StationEntity toEntity(final Station station) {
        return new StationEntity(station.getId(), station.getName());
    }
}
