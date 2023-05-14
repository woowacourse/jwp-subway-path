package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dto.StationRequest;
import subway.dto.StationSelectResponse;
import subway.entity.StationEntity;
import subway.repository.dao.StationDao;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationSelectResponse saveStation(StationRequest stationRequest) {
        StationEntity station = stationDao.insert(new StationEntity(stationRequest.getName()));
        return StationSelectResponse.of(station);
    }

    public StationSelectResponse findStationResponseById(Long id) {
        return StationSelectResponse.of(stationDao.findById(id).get());
    }

    public List<StationSelectResponse> findAllStationResponses() {
        List<StationEntity> stations = stationDao.findAll();

        return stations.stream()
                .map(StationSelectResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.update(new StationEntity(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
