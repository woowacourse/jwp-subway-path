package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Section;

import java.util.Optional;

@Service
public class SectionService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public long insert(long lineId, String previousStationName, String nextStationName, int distance, boolean isDown) {
        Line line = lineDao.findById(lineId);
        Station previousStation = stationDao.findByName(previousStationName);
        Station nextStation = stationDao.findByName(nextStationName);
        if (sectionDao.countStations(line) == 0) {
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
        Optional<Section> nextStations = sectionDao.findByNextStation(section.getNextStation(), section.getLine());
        if (nextStations.isPresent()) {
            return insertUpStations(section, nextStations.get());
        }

        // 2. 등록할 역이 연결할 역에 하행방향으로 달려있는 경우
        Optional<Section> previousStations = sectionDao.findByPreviousStation(section.getPreviousStation(), section.getLine());
        if (previousStations.isPresent()) {
            return insertDownStations(section, previousStations.get());
        }

        // 3. 등록할 역이 맨 앞인 경우
        if (sectionDao.isHighestStationOfLine(section.getNextStation(), section.getLine())) {
            return insertHighestStation(section);
        }

        throw new IllegalArgumentException("등록하는 역과 연결되는 기존의 역 정보가 노선상에 존재하지 않습니다.");
    }

    private long insertHighestStation(Section section) {
        return sectionDao.insert(section).getId();
    }

    private long initialize(Line line, Station previousStation, Station nextStation, int distance, boolean isDown) {
        if (isDown) {
            return sectionDao.initialize(Section.builder()
                    .line(line)
                    .startingStation(previousStation)
                    .before(nextStation)
                    .distance(distance)
                    .build());
        }
        return sectionDao.initialize(Section.builder()
                .line(line)
                .startingStation(previousStation)
                .after(nextStation)
                .distance(distance)
                .build());
    }

    private Long insertDownStations(Section newSection, Section originalSection) {
        Section savedSection = sectionDao.insert(Section.builder()
                .line(newSection.getLine())
                .startingStation(newSection.getNextStation())
                .before(originalSection.getNextStation())
                .distance(originalSection.getDistance() - newSection.getDistance())
                .build());
        sectionDao.update(Section.builder()
                .id(originalSection.getId())
                .line(originalSection.getLine())
                .startingStation(originalSection.getPreviousStation())
                .before(savedSection.getPreviousStation())
                .distance(newSection.getDistance())
                .build());
        return savedSection.getId();
    }

    private Long insertUpStations(Section newSection, Section originalSection) {
        Section savedSection = sectionDao.insert(newSection);
        sectionDao.update(Section.builder()
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

        Optional<Section> stationsToDeleteOptional = sectionDao.findByPreviousStation(station, line);
        Optional<Section> stationsLeftOptional = sectionDao.findByNextStation(station, line);

        if (stationsToDeleteOptional.isPresent() && stationsLeftOptional.isPresent()) {
            Section sectionToDelete = stationsToDeleteOptional.get();
            Section sectionLeft = stationsLeftOptional.get();

            sectionDao.update(Section.builder()
                    .id(sectionLeft.getId())
                    .startingStation(sectionLeft.getPreviousStation())
                    .before(sectionToDelete.getNextStation())
                    .distance(sectionToDelete.getDistance() + sectionLeft.getDistance())
                    .build());
        }

        sectionDao.deleteStation(station, line);
    }

    public int findDistanceBetween(Station stationA, Station stationB, Line line) {
        // 두 역이 current, next 나란히 있는 경우
        Optional<Section> subwayMapOptional = sectionDao.findByPreviousStation(stationA, line);
        if (subwayMapOptional.isPresent() && subwayMapOptional.get().getNextStation().equals(stationB)) {
            return subwayMapOptional.get().getDistance();
        }

        // 두 역이 next, current 이렇게 나란히 있는 경우
        Optional<Section> subwayMapOptional1 = sectionDao.findByNextStation(stationA, line);
        if (subwayMapOptional1.isPresent() && subwayMapOptional1.get().getPreviousStation().equals(stationB)) {
            return subwayMapOptional1.get().getDistance();
        }

        throw new IllegalArgumentException("주어진 두 역이 이웃한 역이 아닙니다.");
        // TODO: 아직까지 이웃하지 않은 역의 거리를 조회하지는 않는다고 가정합니다.
    }
}
