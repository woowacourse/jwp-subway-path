package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.StationDuplicationNameException;
import subway.exception.StationNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station saveStation(final StationRequest stationRequest) {
        stationDao.findByName(stationRequest.getName())
                .ifPresent(ignore -> new StationDuplicationNameException(stationRequest.getName() + "역은 이미 존재합니다."));
        return stationDao.insert(new StationEntity(stationRequest.getName()))
                .toStation();
    }

    private StationEntity findStation(final Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id + " 값을 가진 역은 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<Station> findAllStationResponses() {
        return stationDao.findAll().stream()
                .map(StationEntity::toStation)
                .collect(Collectors.toList());
    }

    public Station updateStation(final Long id, final StationRequest stationRequest) {
        findStation(id);
        final long stationId = stationDao.update(new StationEntity(id, stationRequest.getName()));
        return new Station(stationId, stationRequest.getName());
    }

    public void deleteStationById(final Long id) {
        findStation(id);
        stationDao.deleteById(id);
    }

    public List<Station> findStationsOf(final List<SectionEntity> sectionEntities) {
        final HashSet<String> stationNames = new HashSet<>();
        for (final SectionEntity sectionEntity : sectionEntities) {
            stationNames.add(sectionEntity.getLeft());
            stationNames.add(sectionEntity.getRight());
        }
        return findStationsOf(stationNames);
    }

    private List<Station> findStationsOf(final Set<String> stationNames) {
        return stationDao.findByName(stationNames)
                .stream().map(StationEntity::toStation)
                .collect(Collectors.toList());
    }

    public Station findById(final Long lineId) {
        final StationEntity stationEntity = findStation(lineId);
        return stationEntity.toStation();
    }
}
