package subway.application.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.station.dto.StationCreateResponse;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.ui.dto.StationCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationCreateResponse saveStation(StationCreateRequest stationCreateRequest) {
        Station station = new Station(stationCreateRequest.getName());
        Station savedStation = stationDao.insert(station);
        return StationCreateResponse.of(savedStation);
    }

    @Transactional(readOnly = true)
    public StationCreateResponse findStationById(Long id) {
        return StationCreateResponse.of(stationDao.findById(id));
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
