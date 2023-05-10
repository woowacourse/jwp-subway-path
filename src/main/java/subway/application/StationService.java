package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.domain.station.StationName;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse save(StationRequest stationRequest) {
        StationEntity stationEntity = stationDao.insert(new Station(new StationName(stationRequest.getName())));
        return StationResponse.of(stationEntity);
    }

    public StationResponse findById(Long id) {
        return StationResponse.of(stationDao.findById(id));
    }

    public List<StationResponse> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void update(Long id, StationRequest stationRequest) {
        stationDao.update(id, new Station(new StationName(stationRequest.getName())));
    }

    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }
}
