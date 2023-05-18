package subway.repository.converter;

import subway.entity.StationEntity;
import subway.service.domain.Station;

public class StationConverter {

    private StationConverter(){
    }

    public static Station entityToDomain(StationEntity stationEntity) {
        return new Station(
                stationEntity.getId(),
                stationEntity.getName()
        );
    }

    public static Station entityToDomain(Long id, StationEntity stationEntity) {
        return new Station(
                id,
                stationEntity.getName()
        );
    }

    public static StationEntity domainToEntity(Station station) {
        return new StationEntity(station.getId(), station.getName());
    }

}
