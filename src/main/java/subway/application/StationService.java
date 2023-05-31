package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.DataNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        Long stationId = stationDao.insert(new Station(stationRequest.getName()));
        return findStationById(stationId);
    }

    public StationResponse findStationById(final Long id) {
        Station station = stationDao.findById(id);
        return StationResponse.from(station);
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        int modifiedRow = stationDao.update(new Station(id, stationRequest.getName()));
        if (modifiedRow == 0) {
            throw new DataNotFoundException("존재하지 않는 역입니다");
        }
    }

    public void deleteStationById(final Long id) {
        int modifiedRow = stationDao.deleteById(id);
        if (modifiedRow == 0) {
            throw new DataNotFoundException("존재하지 않는 역입니다");
        }
    }
}