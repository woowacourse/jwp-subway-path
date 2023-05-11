package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.NotFoundException;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        final Stations stations = getStations();
        final Station requestStation = new Station(stationRequest.getName());
        stations.validateDuplication(requestStation);
        final Station savedStation = stationDao.insert(requestStation);
        return StationResponse.of(savedStation.getId(), savedStation.getName());
    }

    public StationResponse findStationResponseById(final Long id) {
        final Station station = getById(id);
        return StationResponse.of(station.getId(), station.getName());
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(station -> StationResponse.of(station.getId(), station.getName()))
                .collect(Collectors.toList());
    }

    public void updateStation(Long id, StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(Long id) {
        stationDao.deleteById(id);
    }

    private Stations getStations() {
        final List<Station> stations = stationDao.findAll();
        return new Stations(stations);
    }

    private Station getById(final Long id) {
        return stationDao.findById(id)
            .orElseThrow(() -> new NotFoundException("해당하는 역이 없습니다"));
    }
}
