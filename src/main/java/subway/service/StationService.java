package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.H2StationDao;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.domain.Stations;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(H2StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Stations stations = Stations.from(stationDao.findAll());
        Station newStation = Station.from(stationRequest.getName());
        stations.addStation(newStation);
        Station insertedStation = stationDao.insert(Station.from(stationRequest.getName()));
        return StationResponse.from(insertedStation);
    }

    public StationResponse findStationById(Long id) {
        return StationResponse.from(stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 역입니다.")));
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationDao.findAll();
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        Station updatedStation = Station.of(id, stationRequest.getName());
        Stations stations = Stations.from(stationDao.findAll());
        stations.removeById(updatedStation.getId());
        stations.addStation(updatedStation);
        stationDao.update(updatedStation);
    }

    public void deleteStationById(Long id) {
        stationDao.findById(id)
                .orElseThrow(() -> new IllegalStateException("[ERROR] 존재하지 않는 역을 삭제할 수 없습니다."));
        stationDao.deleteById(id);
    }
}
