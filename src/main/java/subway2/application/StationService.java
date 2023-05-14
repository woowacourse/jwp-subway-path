package subway2.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway2.dao.StationDao;
import subway2.domain.Station;
import subway2.dto.StationRequest;
import subway2.dto.StationResponse;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        checkDuplicatedStationName(stationRequest);
        Station station = stationDao.insert(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public StationResponse findStationResponseById(Long id) {
        Station station = stationDao.findById(id).orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다."));
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

    private void  checkDuplicatedStationName(StationRequest stationRequest) {
        if (stationDao.findByName(stationRequest.getName()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 역 이름 입니다");
        }
    }
}
