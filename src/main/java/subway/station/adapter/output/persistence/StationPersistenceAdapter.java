package subway.station.adapter.output.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import subway.station.application.port.output.SaveAllStationPort;
import subway.station.application.port.output.SaveStationPort;
import subway.station.domain.Station;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class StationPersistenceAdapter implements SaveStationPort, SaveAllStationPort {
    private final StationDao stationDao;
    
    @Override
    public Long saveStation(final Station station) {
        return saveStationIfNotExist(station.getName());
    }
    
    @Override
    public void saveAll(final List<Station> stations) {
        for (final Station station : stations) {
            saveStationIfNotExist(station.getName());
        }
    }
    
    private Long saveStationIfNotExist(final String stationName) {
        if (isNotExistStation(stationName)) {
            return stationDao.insert(new StationEntity(stationName));
        }
        
        return stationDao.findByName(new Station(stationName)).getId();
    }
    
    private boolean isNotExistStation(final String stationName) {
        return stationDao.findAll().stream()
                .map(StationEntity::getName)
                .noneMatch(perStationName -> perStationName.equals(stationName));
    }
}
