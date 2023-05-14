package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.H2StationDao;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(H2StationDao stationDao) {
        this.stationDao = stationDao;
    }

    //todo: Stations를 이용한 검증

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(Station.from(stationRequest.getName()));
        return StationResponse.from(station);
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
