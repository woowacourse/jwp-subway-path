package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dto.station.StationRequest;
import subway.dto.station.StationSelectResponse;
import subway.entity.StationEntity;
import subway.repository.dao.StationDao;

@Service
public class StationService {
    private final StationDao stationDao;

    private StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationSelectResponse saveStation(StationRequest stationRequest) {
        StationEntity station = stationDao.insert(new StationEntity(stationRequest.getName()));
        return StationSelectResponse.from(station);
    }

    public List<StationSelectResponse> findAllStationResponses() {
        List<StationEntity> stations = stationDao.findAll();

        return stations.stream()
                .map(StationSelectResponse::from)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
