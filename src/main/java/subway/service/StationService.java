package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.controller.dto.request.StationRequest;
import subway.controller.dto.response.StationResponse;
import subway.entity.StationEntity;
import subway.exception.StationNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        StationEntity stationEntity = stationDao.insert(new StationEntity(stationRequest.getName()));
        return StationResponse.of(stationEntity);
    }

    public StationResponse findStationResponseById(Long id) {
        List<StationEntity> stationEntity = stationDao.findById(id);
        if (stationEntity.isEmpty()) {
            throw new StationNotFoundException("해당하는 역을 찾을 수 없습니다.");
        }

        return StationResponse.of(stationEntity.get(0));
    }

    public List<StationResponse> findAllStationResponses() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.update(new StationEntity(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
