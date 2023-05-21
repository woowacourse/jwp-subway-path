package subway.domain.line.service;

import org.springframework.stereotype.Service;
import subway.domain.line.dao.StationDao;
import subway.domain.line.dto.StationCreateRequest;
import subway.domain.line.entity.StationEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationEntity saveStation(final StationCreateRequest stationCreateRequest) {
        Optional<StationEntity> findStation = stationDao.findByName(stationCreateRequest.getName());

        if (findStation.isPresent()) {
            throw new IllegalArgumentException("역 이름이 이미 존재합니다. 유일한 역 이름을 사용해주세요.");
        }

        return stationDao.insert(new StationEntity(stationCreateRequest.getName()));
    }

    public StationEntity findStationById(final Long id) {
        Optional<StationEntity> findStation = stationDao.findById(id);

        if (findStation.isEmpty()) {
            throw new IllegalArgumentException("해당 ID의 역이 존재하지 않습니다.");
        }

        return findStation.get();
    }

    public List<StationEntity> findStationsByIds(final List<Long> id) {
        Optional<List<StationEntity>> findStations = stationDao.findByIds(id);

        if (findStations.isEmpty()) {
            throw new IllegalArgumentException("ID 값중에 존재하지 않는 역이 포함되어 있습니다");
        }

        Map<Long, StationEntity> stationMap = findStations.get().stream()
                .collect(Collectors.toMap(StationEntity::getId, Function.identity()));

        return id.stream()
                .map(stationMap::get)
                .collect(Collectors.toList());
    }

    public List<StationEntity> findAllStation() {
        Optional<List<StationEntity>> findStations = stationDao.findAll();

        if (findStations.isEmpty()) {
            throw new IllegalArgumentException("역이 존재하지 않습니다.");
        }

        return findStations.get();
    }

    public void updateStation(final Long id, final StationCreateRequest stationCreateRequest) {
        Optional<StationEntity> findStation = stationDao.findById(id);

        if (findStation.isEmpty()) {
            throw new IllegalArgumentException("해당 ID의 역이 존재하지 않습니다.");
        }

        stationDao.update(new StationEntity(id, stationCreateRequest.getName()));
    }

    public void deleteStationById(final Long id) {
        Optional<StationEntity> findStation = stationDao.findById(id);

        if (findStation.isEmpty()) {
            throw new IllegalArgumentException("해당 ID의 역이 존재하지 않습니다.");
        }

        stationDao.deleteById(id);
    }
}
