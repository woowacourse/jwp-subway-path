package subway.station.repository;

import subway.station.domain.Station;
import subway.station.entity.StationEntity;

class EntityMapper {

    private EntityMapper() {
    }

    public static StationEntity toEntity(Station station) {
        return new StationEntity.Builder()
                .name(station.getName())
                .build();
    }

    public static Station toDomain(StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }
}
