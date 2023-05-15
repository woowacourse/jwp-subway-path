package subway.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import subway.dao.StationDao;
import subway.domain.Station;
import subway.ui.dto.StationRequest;
import subway.ui.dto.StationResponse;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public StationResponse findStationResponseById(Long id) {
        Station station = stationDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당하는 데이터가 없습니다."));
        return StationResponse.of(station);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
