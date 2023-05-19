package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.request.StationRequest;
import subway.controller.dto.response.StationResponse;
import subway.entity.StationEntity;
import subway.repository.StationDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse save(final StationRequest stationRequest) {
        final StationEntity stationEntity = new StationEntity(stationRequest.getName());
        final StationEntity insertedStationEntity = stationDao.insert(stationEntity);
        return StationResponse.createByEntity(insertedStationEntity);
    }

    public StationResponse findById(final long id) {
        final StationEntity stationEntity = stationDao.findById(id);
        return StationResponse.createByEntity(stationEntity);
    }

    public List<StationResponse> findAll() {
        final List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(StationResponse::createByEntity)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public void update(final long id, final StationRequest stationRequest) {
        final StationEntity stationEntity = new StationEntity(id, stationRequest.getName());
        stationDao.update(stationEntity);
    }

    @Transactional
    public void delete(final long id) {
        stationDao.deleteById(id);
    }
}
