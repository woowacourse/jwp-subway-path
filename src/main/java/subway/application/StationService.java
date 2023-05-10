package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.dto.StationResponse;
import subway.dto.StationSaveRequest;
import subway.entity.Station;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public Long saveStation(StationSaveRequest request) {
        return stationDao.insert(request.toEntity());
    }

    public StationResponse findStationResponseById(Long id) {
        return StationResponse.of(stationDao.findById(id));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationSaveRequest request) {
        stationDao.updateById(id, request.toEntity());
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
