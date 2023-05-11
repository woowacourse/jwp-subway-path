package subway.mapper;

import subway.domain.Station;
import subway.dto.response.StationResponse;
import subway.entity.StationEntity;

public class StationMapper {

    private StationMapper() {
    }

    public static Station toStation(final StationEntity entity) {
        return new Station(entity.getId(), entity.getName());
    }

    public static StationResponse toResponse(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
