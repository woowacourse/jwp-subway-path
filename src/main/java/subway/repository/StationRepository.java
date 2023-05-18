package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.entity.StationEntity;
import subway.exception.StationNotFoundException;
import subway.repository.converter.StationConverter;
import subway.service.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class StationRepository {

    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station save(Station station) {
        StationEntity stationEntity = StationConverter.domainToEntity(station);
        Long id = stationDao.insert(stationEntity);

        return StationConverter.entityToDomain(id, stationEntity);
    }

    public Station findById(Long id) {
        List<StationEntity> stationEntities = stationDao.findById(id);

        if (stationEntities.isEmpty()) {
            throw new StationNotFoundException("해당 역은 존재하지 않습니다.");
        }

        StationEntity stationEntity = stationEntities.get(0);
        return StationConverter.entityToDomain(stationEntity);
    }

    public Station findByName(String name) {
        List<StationEntity> stationEntities = stationDao.findByName(name);

        if (stationEntities.isEmpty()) {
            throw new StationNotFoundException(name + "역은 존재하지 않습니다.");
        }

        StationEntity stationEntity = stationEntities.get(0);
        return StationConverter.entityToDomain(stationEntity);
    }

    public List<Station> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(StationConverter::entityToDomain)
                .collect(Collectors.toList());
    }

}
