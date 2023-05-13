package subway.station.service;

import org.springframework.stereotype.Service;
import subway.station.dao.StationDao;
import subway.station.domain.Station;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        Optional<Station> findStation = stationDao.findByName(stationRequest.getName());

        if (findStation.isPresent()) {
            throw new IllegalArgumentException("역 이름이 이미 존재합니다. 유일한 역 이름을 사용해주세요.");
        }

        final Station station = stationDao.insert(new Station(stationRequest.getName()));
        return StationResponse.of(station);
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
