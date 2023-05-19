package subway.station.adapter.output.persistence;

import org.springframework.stereotype.Repository;
import subway.station.application.port.output.GetStationByIdPort;
import subway.station.application.port.output.SaveAllStationPort;
import subway.station.application.port.output.SaveStationPort;
import subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

@Repository
public class StationPersistenceAdapter implements SaveStationPort, SaveAllStationPort, GetStationByIdPort {
    private final StationDao stationDao;
    
    public StationPersistenceAdapter(final StationDao stationDao) {
        this.stationDao = stationDao;
    }
    
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
        if (isNotExistStationByName(stationName)) {
            return stationDao.insert(new StationEntity(stationName));
        }
        
        return stationDao.findByName(new Station(stationName)).getId();
    }
    
    private boolean isNotExistStationByName(final String stationName) {
        return stationDao.findAll().stream()
                .map(StationEntity::getName)
                .noneMatch(perStationName -> perStationName.equals(stationName));
    }
    
    @Override
    public Station getStationById(final Long id) {
        return stationDao.findAll().stream()
                .filter(stationEntity -> Objects.equals(stationEntity.getId(), id))
                .map(stationEntity -> new Station(stationEntity.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("stationId에 해당하는 역이 존재하지 않습니다."));
    }
}
