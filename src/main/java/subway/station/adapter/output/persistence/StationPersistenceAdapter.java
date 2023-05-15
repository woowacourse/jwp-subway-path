package subway.station.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import subway.station.application.port.output.SaveAllStationPort;
import subway.station.domain.Station;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class StationPersistenceAdapter implements SaveAllStationPort {
    private final StationDao stationDao;
    
    @Override
    public void saveAll(final List<Station> stations) {
        for (final Station station : stations) {
            saveStationIfNotExist(station.getName());
        }
    }
    
    private void saveStationIfNotExist(final String stationName) {
        if (isNotExistStation(stationName)) {
            stationDao.insert(new StationEntity(stationName));
        }
    }
    
    private boolean isNotExistStation(final String stationName) {
        return stationDao.findAll().stream()
                .map(StationEntity::getName)
                .noneMatch(perStationName -> perStationName.equals(stationName));
    }
}
