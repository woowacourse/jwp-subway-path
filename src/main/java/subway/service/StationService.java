package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.entity.StationEntity;
import subway.exception.NoSuchStationException;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        StationEntity stationEntity = stationDao.insert(new StationEntity(stationRequest.getName()));
        return StationResponse.of(stationEntity);
    }

    public StationResponse findStationResponseById(Long id) {
        StationEntity station = stationDao.findById(id)
                .orElseThrow(NoSuchStationException::new);
        return StationResponse.of(station);
    }

    public List<StationResponse> findAllStationResponses() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.update(new StationEntity(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
