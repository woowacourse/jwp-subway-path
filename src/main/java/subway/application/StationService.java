package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationUpdateRequest;

@Service
@Transactional
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public void saveStation(StationCreateRequest stationCreateRequest) {
        stationDao.insert(new StationEntity(stationCreateRequest.getStationName()));
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll()
                .stream()
                .map(entity -> new Station(entity.getName()))
                .collect(Collectors.toList());
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationUpdateRequest stationUpdateRequest) {
        StationEntity station = stationDao.findById(id);
        StationEntity stationEntity = new StationEntity(station.getId(), stationUpdateRequest.getStationName());
        stationDao.update(stationEntity);
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
