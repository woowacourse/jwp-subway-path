package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

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

    public Station findStationById(final Long id) {
        return stationDao.findById(id);
    }

    public List<Station> findAllStation() {
        return stationDao.findAll();
    }

    public StationResponse findStationResponseById(final Long id) {
        return StationResponse.of(stationDao.findById(id));
    }

    public List<StationResponse> findAllStationResponses() {
        final List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(final Long id) {
        stationDao.deleteById(id);
    }
}
