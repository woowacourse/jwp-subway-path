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

    public StationResponse findStationResponseById(Long id) {
        return StationResponse.from(stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다.")));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    //todo : Station 이름 검증 후 DB 삽입하도록 수정
    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.update(Station.of(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
