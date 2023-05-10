package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationCreateRequest;
import subway.dto.StationResponse;
import subway.dto.StationUpdateRequest;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public void saveStation(StationCreateRequest stationCreateRequest) {
        stationDao.insert(new Station(stationCreateRequest.getStationName()));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void updateStation(String prevStationName, StationUpdateRequest stationUpdateRequest) {
        stationDao.update(prevStationName, new Station(stationUpdateRequest.getStationName()));
    }

    public void deleteStationByName(String stationName) {
        stationDao.deleteByName(stationName);
    }
}
