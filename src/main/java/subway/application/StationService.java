package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.StationDuplicationNameExcpetion;
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

    public StationResponse saveStation(final StationRequest stationRequest) {
        stationDao.findByName(stationRequest.getName())
                .ifPresent(ignore -> new StationDuplicationNameExcpetion(stationRequest.getName() + "역은 이미 존재합니다."));
        final StationEntity station = stationDao.insert(new StationEntity(stationRequest.getName()));
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public StationResponse findStationResponseById(final Long id) {
        final StationEntity stationEntity = findStation(id);
        return StationResponse.of(stationEntity);
    }

    private StationEntity findStation(final Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id + " 값을 가진 역은 없습니다."));
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        final List<StationEntity> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public StationResponse updateStation(final Long id, final StationRequest stationRequest) {
        findStation(id);
        final long stationId = stationDao.update(new StationEntity(id, stationRequest.getName()));
        return new StationResponse(stationId, stationRequest.getName());
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
