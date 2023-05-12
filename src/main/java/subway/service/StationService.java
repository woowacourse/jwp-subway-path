package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.StationDao;
import subway.domain.Station;
import subway.dto.StationCreateRequest;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    public Station saveStation(StationCreateRequest createRequest) {
        Station station = new Station(createRequest.getStationName());
        if (stationDao.findByName(station).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 지하철역 이름입니다.");
        }
        return stationDao.insert(station);
    }
}
