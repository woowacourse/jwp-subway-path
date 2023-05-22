package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        Station newStation = Station.from(stationRequest.getName());
        validateDuplicatedName(newStation);
        return StationResponse.from(stationDao.insert(Station.from(stationRequest.getName())));
    }

    public StationResponse findStationById(final long id) {
        return StationResponse.from(stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 역입니다.")));
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationDao.findAll();
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void updateStation(final long id, final StationRequest stationRequest) {
        Station oldStation = stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 역을 수정할 수 없습니다."));
        Station updatedStation = Station.of(id, stationRequest.getName());
        if (!oldStation.isSameName(updatedStation)) {
            validateDuplicatedName(updatedStation);
        }
        stationDao.update(updatedStation);
    }

    private void validateDuplicatedName(final Station updatedStation) {
        if (stationDao.countByName(updatedStation.getName()) != 0) {
            throw new IllegalArgumentException("[ERROR] 중복된 역 이름을 등록할 수 없습니다.");
        }
    }

    public void deleteStationById(final long id) {
        stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 역을 삭제할 수 없습니다."));
        stationDao.deleteById(id);
    }
}
