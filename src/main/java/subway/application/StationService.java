package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.entity.StationEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long save(StationRequest stationRequest) {
        return stationDao.insert(new StationEntity(stationRequest.getName()));
    }

    public StationResponse findById(Long id) {
        StationEntity station = stationDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 역이 존재하지 않습니다."));
        return StationResponse.of(station);
    }

    public List<StationResponse> findAll() {
        List<StationEntity> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void update(Long id, StationRequest stationRequest) {
        stationDao.update(id, new StationEntity(stationRequest.getName()));
    }

    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }
}
