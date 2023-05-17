package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.persistance.StationDao;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        final Station station = stationDao.insert(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(final Long id) {
        stationDao.deleteById(id);
    }

    public StationResponse findStationResponseById(final Long id) {
        return StationResponse.of(findById(id));
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStationResponses() {
        return stationDao.findAll().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Station findById(final Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 역이 존재하지 않습니다."));
    }
}
