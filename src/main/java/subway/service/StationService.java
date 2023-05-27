package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Entity.StationEntity;
import subway.controller.exception.OptionalHasNoStationException;
import subway.domain.station.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.persistence.dao.StationDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        StationEntity stationEntity = stationDao.insert(Station.from(stationRequest.getName()));
        return StationResponse.of(stationEntity);
    }

    public StationResponse findStationResponseById(Long id) {
        StationEntity stationEntity = stationDao.findById(id)
                .orElseThrow(OptionalHasNoStationException::new);
        return StationResponse.of(stationEntity);
    }

    public List<StationResponse> findAllStationResponses() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        StationEntity stationEntity = new StationEntity(id, stationRequest.getName());
        stationDao.update(stationEntity);
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }
}
