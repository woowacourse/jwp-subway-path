package subway.Entity;

import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

public class EntityMapper {

    public static List<StationEntity> convertToStationEntities(
            List<Station> stations,
            List<StationEntity> stationEntities
    ) {
        List<StationEntity> convertedStationEntities = new ArrayList<>();
        stations.forEach(station -> convertedStationEntities.add(matchStationToEntity(station, stationEntities)));

        return convertedStationEntities;
    }

    private static StationEntity matchStationToEntity(Station station, List<StationEntity> stationEntities) {
        return stationEntities.stream()
                .filter(stationEntity -> station.isSameName(stationEntity.mapToStation()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 영속화되지 않은 역이 존재합니다."));
    }
}
