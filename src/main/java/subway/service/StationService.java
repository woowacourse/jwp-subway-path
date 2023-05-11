package subway.service;

import org.springframework.stereotype.Service;
import subway.persistence.dao.StationDao;
import subway.domain.Station;
import subway.service.dto.StationRequest;
import subway.service.dto.StationResponse;

import java.util.LinkedList;
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

    public Station findStationByName(final String name) {
        return stationDao.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("역을 찾을 수 없습니다."));
    }

    public List<Station> findStationsById(final List<Long> stationIds) {
        if (stationIds.isEmpty()) {
            return new LinkedList<>();
        }
        return stationDao.findAllById(stationIds);
    }
}
