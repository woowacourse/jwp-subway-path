package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dao.StationsDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Section;

import java.util.Optional;

@Service
public class SectionService {
    private final StationsDao stationsDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionService(StationsDao stationsDao, StationDao stationDao, LineDao lineDao) {
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
            return insert(Section.builder()
                    .line(line)
                    .startingStation(previousStation)
                    .before(nextStation)
                    .distance(distance)
                    .build());
        }
        return insert(Section.builder()
                .line(line)
                .startingStation(previousStation)
                .after(nextStation)
                .distance(distance)
                .build());
    }

    private long insert(Section section) {
        // 1. 등록할 역이 연결할 역에 상행방향으로 달려있는 경우
        Optional<Section> nextStations = stationsDao.findByNextStation(section.getNextStation(), section.getLine());
        if (nextStations.isPresent()) {
            return insertUpStations(section, nextStations.get());
        }

        // 2. 등록할 역이 연결할 역에 하행방향으로 달려있는 경우
        Optional<Section> previousStations = stationsDao.findByPreviousStation(section.getPreviousStation(), section.getLine());
        if (previousStations.isPresent()) {
            return insertDownStations(section, previousStations.get());
        }

        // 3. 등록할 역이 맨 앞인 경우
        if (stationsDao.isHighestStationOfLine(section.getNextStation(), section.getLine())) {
            return insertHighestStation(section);
        }

        throw new IllegalArgumentException("등록하는 역과 연결되는 기존의 역 정보가 노선상에 존재하지 않습니다.");
    }

    private long insertHighestStation(Section section) {
        return stationsDao.insert(section).getId();
    }

    private long initialize(Line line, Station previousStation, Station nextStation, int distance, boolean isDown) {
        if (isDown) {
            return stationsDao.initialize(Section.builder()
                    .line(line)
                    .startingStation(previousStation)
                    .before(nextStation)
                    .distance(distance)
                    .build());
        }
        return stationsDao.initialize(Section.builder()
                .line(line)
                .startingStation(previousStation)
                .after(nextStation)
                .distance(distance)
                .build());
    }

    private Long insertDownStations(Section newSection, Section originalSection) {
        Section savedSection = stationsDao.insert(Section.builder()
                .line(newSection.getLine())
                .startingStation(newSection.getNextStation())
                .before(originalSection.getNextStation())
                .distance(originalSection.getDistance() - newSection.getDistance())
                .build());
        stationsDao.update(Section.builder()
                .id(originalSection.getId())
                .line(originalSection.getLine())
                .startingStation(originalSection.getPreviousStation())
                .before(savedSection.getPreviousStation())
                .distance(newSection.getDistance())
                .build());
        return savedSection.getId();
    }

    private Long insertUpStations(Section newSection, Section originalSection) {
        Section savedSection = stationsDao.insert(newSection);
        stationsDao.update(Section.builder()
                .id(originalSection.getId())
                .line(newSection.getLine())
                .startingStation(originalSection.getPreviousStation())
                .before(newSection.getPreviousStation())
                .distance(originalSection.getDistance() - newSection.getDistance())
                .build()
        );
        return savedSection.getId();
    }

    public void deleteStation(long lineId, String stationName) {
        Line line = lineDao.findById(lineId);
        Station station = stationDao.findByName(stationName);
        stationsDao.deleteStation(station, line);
    }
}
