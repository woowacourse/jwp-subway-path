package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.entity.Station;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long save(StationRequest stationRequest) {
        return stationDao.insert(new Station(stationRequest.getName()));
    }

    public StationResponse findById(Long id) {
        Optional<Station> station = stationDao.findById(id);
        if (station.isEmpty()) {
            throw new NoSuchElementException("해당하는 역이 존재하지 않습니다.");
        }
        return StationResponse.of(station.get());
    }

    public List<StationResponse> findAll() {
        List<Station> stationEntities = stationDao.findAll();

        return stationEntities.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void update(Long id, StationRequest stationRequest) {
        stationDao.update(id, new Station(stationRequest.getName()));
    }

    public void deleteById(Long id) {
        stationDao.deleteById(id);
    }
}
