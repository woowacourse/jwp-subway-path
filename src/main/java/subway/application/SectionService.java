package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dao.SectionDao;
import subway.domain.*;

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

    public long insert(long lineId, String previousStationName, String nextStationName, Distance distance, boolean isDown) {
        Line line = lineDao.findById(lineId);
        Station previousStation = stationDao.findByName(previousStationName);
        Station nextStation = stationDao.findByName(nextStationName);
        if (sectionDao.isLineEmpty(line)) {
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
            if (previousStations.get().isNextStationEmpty()) {
                // 3. 등록할 역이 맨 뒤인 경우
                return insertLowestStation(section);
            }
            return insertDownStations(section, previousStations.get());
        }

        // 4. 등록할 역이 맨 앞인 경우
        if (lineDao.findHeadStation(section.getLine()).equals(section.getNextStation())) {
            return insertHighestStation(section);
        }

        throw new IllegalArgumentException("등록하는 역과 연결되는 기존의 역 정보가 노선상에 존재하지 않습니다.");
    }

    private long insertLowestStation(Section section) {
        Optional<Section> previousLowestSection = sectionDao.findByPreviousStation(section.getPreviousStation(), section.getLine());
        if (previousLowestSection.isEmpty()) {
            throw new IllegalArgumentException("원래 하행 종점역의 정보를 찾을 수 없습니다.");
        }

        Section newSection = sectionDao.insert(Section.builder()
                .line(section.getLine())
                .nextStationEmpty(section.getNextStation())
                .build());

        sectionDao.update(Section.builder()
                .id(previousLowestSection.get().getId())
                .line(section.getLine())
                .nextStationEmpty(section.getPreviousStation())
                .build());

        return newSection.getId();
    }

    private long insertHighestStation(Section section) {
        final var sectionId = sectionDao.insert(section).getId();
        lineDao.updateHeadStation(section.getLine(), section.getPreviousStation());
        return sectionId;
    }

    private long initialize(Line line, Station previousStation, Station nextStation, Distance distance, boolean isDown) {
        if (!sectionDao.isLineEmpty(line)) {
            throw new IllegalArgumentException("이미 초기 설정이 완료된 노선입니다.");
        }

        if (isDown) {
            Long savedId = sectionDao.insert(Section.builder()
                    .line(line)
                    .startingStation(previousStation)
                    .before(nextStation)
                    .distance(distance)
                    .build()).getId();

            sectionDao.insert(Section.builder()
                    .line(line)
                    .nextStationEmpty(nextStation)
                    .build());

            lineDao.updateHeadStation(line, previousStation);
            return savedId;
        }

        Long saveId = sectionDao.insert(Section.builder()
                .line(line)
                .startingStation(previousStation)
                .after(nextStation)
                .distance(distance)
                .build()).getId();

        sectionDao.insert(Section.builder()
                .line(line)
                .nextStationEmpty(previousStation)
                .build());

        lineDao.updateHeadStation(line, nextStation);
        return saveId;
    }

    private Long insertDownStations(Section newSection, Section originalSection) {
        // 새로운 역 :
        Section savedSection = sectionDao.insert(Section.makeSectionToInsertDownDirection(newSection, originalSection));
        sectionDao.update(Section.makeSectionToUpdateDownDirection(newSection, originalSection));
        return savedSection.getId();
    }

    private Long insertUpStations(Section newSection, Section originalSection) {
        Section savedSection = sectionDao.insert(newSection);
        sectionDao.update(Section.makeSectionToUpdateUpDirection(newSection, originalSection));
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

            sectionDao.update(Section.makeSectionToUpdateAfterDeletion(sectionLeft, sectionToDelete));
        }

        sectionDao.deleteStation(station, line);
    }

    public Distance findDistanceBetween(Station stationA, Station stationB, Line line) {
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
