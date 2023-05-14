package subway.application;

import org.springframework.stereotype.Service;
import subway.application.strategy.sectioninsertion.*;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;
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

        final var section = isDown ?
                new Section(line, previousStation, nextStation, distance)
                : new Section(line, nextStation, previousStation, distance);

        final var strategies = List.of(
                new InitializingSectionInsertionStrategy(sectionDao, lineDao),
                new UpDirectionSectionInsertionStrategy(sectionDao),
                new LowestSectionInsertionStrategy(sectionDao),
                new DownDirectionInsertionStrategy(sectionDao),
                new HighestSectionInsertionStrategy(sectionDao, lineDao)
        );

        for (SectionInsertionStrategy strategy : strategies) {
            if (strategy.support(section)) {
                return strategy.insert(section);
            }
        }

        throw new IllegalArgumentException("등록하는 역과 연결되는 기존의 역 정보가 노선상에 존재하지 않습니다.");
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
