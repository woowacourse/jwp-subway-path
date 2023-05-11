package subway.domain.station;

import subway.entity.SectionStationJoinEntity;
import subway.entity.StationEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toUnmodifiableMap;

public class StationConnections {

    private final Map<StationEntity, StationEntity> stationConnections;

    private StationConnections(Map<StationEntity, StationEntity> stationConnections) {
        this.stationConnections = stationConnections;
    }

    public static StationConnections fromEntities(List<SectionStationJoinEntity> sectionStationJoinEntities) {
        Map<StationEntity, StationEntity> stationConnections = sectionStationJoinEntities.stream()
                .collect(toUnmodifiableMap(
                        e1 -> new StationEntity(e1.getUpStationId(), e1.getUpStationName(), e1.getLineId()),
                        e1 -> new StationEntity(e1.getDownStationId(), e1.getDownStationName(), e1.getLineId())
                ));
        return new StationConnections(stationConnections);
    }

    public List<String> getSortedStationNames() {
        List<String> sortedStationNames = new ArrayList<>();
        StationEntity upEndStation = findUpEndStation();
        sortedStationNames.add(upEndStation.getName());

        StationEntity tempUpStation = upEndStation;
        for (int i = 0; i < stationConnections.size(); i++) {
            StationEntity downStation = stationConnections.get(tempUpStation);
            sortedStationNames.add(downStation.getName());
            tempUpStation = downStation;
        }
        return sortedStationNames;
    }

    private StationEntity findUpEndStation() {
        List<StationEntity> upStations = new ArrayList<>(stationConnections.keySet());
        List<StationEntity> downStations = new ArrayList<>(stationConnections.values());
        upStations.removeAll(downStations);
        if (upStations.size() != 1) {
            throw new IllegalStateException("상행 종점을 찾을 수 없습니다.");
        }
        return upStations.get(0);
    }
}
