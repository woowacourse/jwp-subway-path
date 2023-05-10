package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dao.StationsDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Stations;

import java.util.Optional;

@Service
public class StationsService {
    private final StationsDao stationsDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public StationsService(StationsDao stationsDao, StationDao stationDao, LineDao lineDao) {
        this.stationsDao = stationsDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public long insert(long lineId, String previousStationName, String nextStationName, int distance, boolean isDown) {
        Line line = lineDao.findById(lineId);
        Station previousStation = stationDao.findByName(previousStationName);
        Station nextStation = stationDao.findByName(nextStationName);
        if (stationsDao.countStations(line) == 0) {
            return initialize(line, previousStation, nextStation, distance, isDown);
        }

        if (isDown) {
            return insert(Stations.builder()
                    .line(line)
                    .startingStation(previousStation)
                    .before(nextStation)
                    .distance(distance)
                    .build());
        }
        return insert(Stations.builder()
                .line(line)
                .startingStation(previousStation)
                .after(nextStation)
                .distance(distance)
                .build());
    }

    private long insert(Stations stations) {
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

    private long initialize(Line line, Station previousStation, Station nextStation, int distance, boolean isDown) {
        if (isDown) {
            return stationsDao.initialize(Stations.builder()
                    .line(line)
                    .startingStation(previousStation)
                    .before(nextStation)
                    .distance(distance)
                    .build());
        }
        return stationsDao.initialize(Stations.builder()
                .line(line)
                .startingStation(previousStation)
                .after(nextStation)
                .distance(distance)
                .build());
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

    public void deleteStation(long lineId, String stationName) {
        Line line = lineDao.findById(lineId);
        Station station = stationDao.findByName(stationName);
        stationsDao.deleteStation(station, line);
    }
}
