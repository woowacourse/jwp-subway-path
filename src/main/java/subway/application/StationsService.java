package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.StationsDao;
import subway.domain.Stations;

import java.util.Optional;

@Service
public class StationsService {
    private final StationsDao stationsDao;

    public StationsService(StationsDao stationsDao) {
        this.stationsDao = stationsDao;
    }

    public long insert(Stations stations) {
        // 1. 등록할 역이 연결할 역에 상행방향으로 달려있는 경우
        Optional<Stations> nextStations = stationsDao.findByNextStation(stations.getNextStation(), stations.getLine());
        if (nextStations.isPresent()) {
            return insertUpStations(stations, nextStations.get());
        }

        // 2. 등록할 역이 연결할 역에 하행방향으로 달려있는 경우
        Optional<Stations> previousStations = stationsDao.findByPreviousStation(stations.getPreviousStation(), stations.getLine());
        if (previousStations.isPresent()) {
            return insertDownStations(stations, previousStations.get());
        }

        throw new IllegalArgumentException("등록하는 역과 연결되는 기존의 역 정보가 노선상에 존재하지 않습니다.");
    }

    private Long insertDownStations(Stations newStations, Stations originalStations) {
        Stations savedStations = stationsDao.insert(Stations.builder()
                .line(newStations.getLine())
                .startingStation(newStations.getNextStation())
                .before(originalStations.getNextStation())
                .distance(originalStations.getDistance() - newStations.getDistance())
                .build());
        stationsDao.update(Stations.builder()
                .id(originalStations.getId())
                .line(originalStations.getLine())
                .startingStation(originalStations.getPreviousStation())
                .before(savedStations.getPreviousStation())
                .distance(newStations.getDistance())
                .build());
        return savedStations.getId();
    }

    private Long insertUpStations(Stations newStations, Stations originalStations) {
        Stations savedStations = stationsDao.insert(newStations);
        stationsDao.update(Stations.builder()
                .id(originalStations.getId())
                .line(newStations.getLine())
                .startingStation(originalStations.getPreviousStation())
                .before(newStations.getPreviousStation())
                .distance(originalStations.getDistance() - newStations.getDistance())
                .build()
        );
        return savedStations.getId();
    }
}
