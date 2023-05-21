package subway.application.station;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationResponse;
import subway.dto.station.StationUpdateRequest;
import subway.exception.station.DuplicateStationException;
import subway.exception.station.StationNotFoundException;

@Service
@Transactional
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public void saveStation(StationCreateRequest stationCreateRequest) {
        if (stationDao.existsBy(stationCreateRequest.getStationName())) {
            throw new DuplicateStationException();
        }
        stationDao.insert(new Station(stationCreateRequest.getStationName()));
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void updateStation(String prevStationName, StationUpdateRequest stationUpdateRequest) {
        if (stationDao.doesNotExistBy(prevStationName)) {
            throw new StationNotFoundException();
        }
        stationDao.update(prevStationName, new Station(stationUpdateRequest.getStationName()));
    }

    public void deleteStationByName(String stationName) {
        stationDao.deleteByName(stationName);
    }
}
