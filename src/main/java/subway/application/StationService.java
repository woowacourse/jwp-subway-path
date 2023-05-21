package subway.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.entity.StationEntity;

@Service
@Transactional
public class StationService {

    private static final String NO_SUCH_STATION_MESSAGE = "해당하는 역이 존재하지 않습니다.";

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Long save(final StationRequest stationRequest) {
        final Station station = new Station(stationRequest.getName());
        return stationDao.insert(StationEntity.from(station));
    }

    public StationResponse findById(final Long id) {
        final Station station = stationDao.findById(id)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_STATION_MESSAGE))
                .toDomain();
        return StationResponse.of(station);
    }

    public List<StationResponse> findAll() {
        return stationDao.findAll().stream()
                .map(it -> StationResponse.of(it.toDomain()))
                .collect(Collectors.toList());
    }

    public void update(final Long id, final StationRequest stationRequest) {
        final Station station = new Station(stationRequest.getName());
        stationDao.update(id, StationEntity.from(station));
    }

    public void deleteById(final Long id) {
        stationDao.deleteById(id);
    }
}
