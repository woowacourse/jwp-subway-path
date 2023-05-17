package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.persistence.entity.StationEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StationRepository {
    private final StationDao stationDao;

    public StationRepository(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        StationEntity station = stationDao.insert(new StationEntity(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public StationResponse findStationResponseById(Long id) {
        Optional<StationEntity> stationEntity = stationDao.findById(id);
        if (stationEntity.isPresent()) {
            return StationResponse.of(stationEntity.get());
        }
        throw new IllegalArgumentException("존재하지 않는 역입니다");
    }

    public List<StationResponse> findAllStationResponses() {
        List<StationEntity> stations = stationDao.findAll();

        return stations.stream()
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
