package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.entity.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

@Service
public class StationService {

    private static final String EXCEPTION_MESSAGE_STATION_ID_NOT_FOUND = "해당 Id를 가진 역 정보가 존재하지 않습니다.";
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationDao.insert(new Station(stationRequest.getName()));
        return StationResponse.from(station);
    }

    public StationResponse findStationResponseById(Long id) {
        Station found = stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(EXCEPTION_MESSAGE_STATION_ID_NOT_FOUND));
        return StationResponse.from(found);
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
