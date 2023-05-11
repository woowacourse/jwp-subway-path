package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

//    public StationResponse saveStation(StationRequest stationRequest) {
//        StationEntity station = stationDao.insert(new Station(stationRequest.getName()));
//        return StationResponse.of(station);
//    }

//    public StationResponse findStationResponseById(Long id) {
//        return StationResponse.of(stationDao.findById(id));
//    }

    public List<StationEntity> findAllStationTest() {
        return stationDao.findAll();
    }

    public List<StationResponse> findAllStationResponses(Long lineId, Long headStationId) {
        StationEntity headEntity = stationDao.findById(headStationId);
        List<StationEntity> entities = stationDao.findByLineId(lineId);
        // TODO: 2023/05/11 headEntity가 entities에 포함되어 있지 않을 경우 예외처리

        List<Station> stations = Station.from(entities, headEntity);
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
