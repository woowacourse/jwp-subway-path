package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.domain.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        checkDuplicatedStationName(stationRequest);
        StationEntity station = stationDao.insert(new StationEntity(stationRequest.getName()));
        return StationResponse.of(Station.from(station));
    }

    public StationResponse findStationResponseById(Long id) {
        StationEntity station = stationDao.findById(id)
                                          .orElseThrow(() -> new NotFoundException("해당 역이 존재하지 않습니다."));
        return StationResponse.of(Station.from(station));
    }

    public List<StationResponse> findAllStationResponses() {
        List<StationEntity> stationEntities = stationDao.findAll();

        List<Station> stations = stationEntities.stream()
                                                .map(Station::from)
                                                .collect(Collectors.toList());

        return stations.stream()
                       .map(StationResponse::of)
                       .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.update(new StationEntity(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        isExist(id);
        stationDao.deleteById(id);
    }

    private void checkDuplicatedStationName(StationRequest stationRequest) {
        if (stationDao.isExistName(stationRequest.getName())) {
            throw new IllegalArgumentException("이미 존재하는 역 이름 입니다");
        }
    }

    private void isExist(Long id) {
        if (stationDao.isExistId(id)) {
            throw new NotFoundException("해당 역은 존재하지 않습니다.");
        }
    }
}
