package subway.domain.station.service;

import org.springframework.stereotype.Service;
import subway.domain.station.dao.StationDao;
import subway.domain.station.domain.Station;
import subway.domain.station.dto.StationRequest;

import java.util.List;
import java.util.Optional;

@Service
public class StationService {
    private final StationDao stationDao;

    public StationService(final StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station saveStation(final StationRequest stationRequest) {
        Optional<Station> findStation = stationDao.findByName(stationRequest.getName());

        if (findStation.isPresent()) {
            throw new IllegalArgumentException("역 이름이 이미 존재합니다. 유일한 역 이름을 사용해주세요.");
        }

        return stationDao.insert(new Station(stationRequest.getName()));
    }

    public Station findStationById(final Long id) {
        return stationDao.findById(id);
    }

    public List<Station> findAllStationResponses() {
        return stationDao.findAll();
    }

    public void updateStation(final Long id, final StationRequest stationRequest) {
        stationDao.update(new Station(id, stationRequest.getName()));
    }

    public void deleteStationById(final Long id) {
        stationDao.deleteById(id);
    }
}
