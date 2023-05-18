package subway.service;

import org.springframework.stereotype.Service;
import subway.service.converter.station.StationEntityRequestResponseConverter;
import subway.repository.StationDao;
import subway.entity.StationEntity;
import subway.controller.dto.request.StationRequest;
import subway.controller.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse save(final StationRequest stationRequest) {
        final StationEntity stationEntity = StationEntityRequestResponseConverter.toEntity(stationRequest);
        final StationEntity insertedStationEntity = stationDao.insert(stationEntity);
        return StationEntityRequestResponseConverter.toResponse(insertedStationEntity);
    }

    public StationResponse findById(final long id) {
        final StationEntity stationEntity = stationDao.findById(id);
        return StationEntityRequestResponseConverter.toResponse(stationEntity);
    }

    public List<StationResponse> findAll() {
        final List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(StationEntityRequestResponseConverter::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public void update(final long id, final StationRequest stationRequest) {
        final StationEntity stationEntity = new StationEntity(id, stationRequest.getName());
        stationDao.update(stationEntity);
    }

    public void delete(final long id) {
        stationDao.deleteById(id);
    }

}
