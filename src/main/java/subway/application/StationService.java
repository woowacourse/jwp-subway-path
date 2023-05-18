package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

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
        final StationEntity station = stationDao.insert(new StationEntity(stationRequest.getName()));
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public StationResponse findStationResponseById(final Long id) {
        return StationResponse.of(stationDao.findById(id));
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        final List<StationEntity> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        stationDao.update(new StationEntity(id, stationRequest.getName()));
    }

    public void deleteStationById(final Long id) {
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

    public Station findById(final Long fromStation) {
        return stationDao.findById(fromStation).toStation();
    }
}
