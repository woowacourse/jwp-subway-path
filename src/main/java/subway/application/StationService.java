package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.subwaymap.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse create(final StationRequest stationRequest) {
        final Station station = stationDao.insert(Station.from(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public StationResponse findById(final Long id) {
        return StationResponse.of(stationDao.findById(id));
    }

    public List<StationResponse> findAll() {
        final List<Station> stations = stationDao.findAll();

        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    public void update(final Long id, final StationRequest stationRequest) {
        stationDao.update(Station.of(id, stationRequest.getName()));
    }

    public void deleteById(final Long id) {
        stationDao.deleteById(id);
    }
}
