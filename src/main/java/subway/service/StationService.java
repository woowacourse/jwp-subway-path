package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
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

    public StationResponse saveStation(StationRequest stationRequest) {
        StationEntity stationEntity = createEntityFromRequest(stationRequest);
        Long id = stationDao.insert(stationEntity);
        return new StationResponse(id, stationEntity.getName());
    }

    private StationEntity createEntityFromRequest(StationRequest stationRequest) {
        //역 하나를 생성할 때는 당연히 이름만 설정 가능하다
        return new StationEntity(null, stationRequest.getName(), null, null, null);
    }

    public List<StationResponse> findAllStationResponses() {
        List<StationEntity> stationEntities = stationDao.findAll();
        return stationEntities.stream()
                .map(this::convertToStationResponse)
                .collect(Collectors.toList());
    }

    private StationResponse convertToStationResponse(StationEntity stationEntity) {
        return new StationResponse(stationEntity.getId(), stationEntity.getName());
    }

    public StationResponse findStationResponseById(Long id) {
        StationEntity stationEntity = stationDao.findStationEntityById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 역이 존재하지 않습니다"));

        return convertToStationResponse(stationEntity);
    }

    public Long updateStation(Long id, StationRequest stationRequest) {
        StationEntity stationEntity = stationDao.findStationEntityById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 역이 존재하지 않습니다"));

        return stationDao.update(id, stationEntity);
    }

    public Long deleteStationById(Long id) {
        StationEntity stationEntity = stationDao.findStationEntityById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 역이 존재하지 않습니다"));

        return stationDao.remove(id);
    }
}
