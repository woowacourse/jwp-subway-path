package subway.application;

import org.springframework.stereotype.Service;
import subway.application.exception.ExceptionMessages;
import subway.application.exception.StationNotConnectedException;
import subway.application.exception.StationNotRegisteredException;
import subway.application.strategy.sectioninsertion.SectionInsertionStrategy;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;
    private final List<SectionInsertionStrategy> strategies;

    public SectionService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao, List<SectionInsertionStrategy> strategies) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.strategies = strategies;
    }

    public long insert(long lineId, String previousStationName, String nextStationName, Distance distance, boolean isDown) {
        Line line = lineDao.findById(lineId);
        Station previousStation = stationDao.findByName(previousStationName);
        Station nextStation = stationDao.findByName(nextStationName);

        final var section = isDown ?
                new Section(line, previousStation, nextStation, distance)
                : new Section(line, nextStation, previousStation, distance);

        for (SectionInsertionStrategy strategy : strategies) {
            if (strategy.support(section)) {
                return strategy.insert(section);
            }
        }

        throw new StationNotRegisteredException(ExceptionMessages.STATION_NOT_REGISTERED);
    }

    public void deleteStation(long lineId, String stationName) {
        Line line = lineDao.findById(lineId);
        Station station = stationDao.findByName(stationName);

        if (sectionDao.countStations(line) == Section.MIN_STATION_COUNT) {
            sectionDao.clearStations(line);
            return;
        }

        Optional<Section> stationsToDeleteOptional = sectionDao.findByPreviousStation(station, line);
        Optional<Section> stationsLeftOptional = sectionDao.findByNextStation(station, line);

        if (stationsToDeleteOptional.isPresent() && stationsLeftOptional.isPresent()) {
            Section sectionToDelete = stationsToDeleteOptional.get();
            Section sectionLeft = stationsLeftOptional.get();

            sectionDao.update(new Section(sectionLeft.getId(), sectionLeft.getLine(), sectionLeft.getPreviousStation(), sectionToDelete.getNextStation(), sectionToDelete.getDistance().add(sectionLeft.getDistance())));
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

        throw new StationNotConnectedException(ExceptionMessages.STATION_NOT_CONNECTED);
        // TODO: 아직까지 이웃하지 않은 역의 거리를 조회하지는 않는다고 가정합니다.
    }

    public List<Station> findAllStationsOrderByUp(Line line) {
        final var stations = new ArrayList<Station>();
        final var sections = sectionDao.findAllByLine(line);

        var station = line.getHead();
        while (station != null) {
            Station currentStation = station;

            final var nextStation = sections.stream()
                    .filter(section -> section.getPreviousStation().equals(currentStation))
                    .findAny();

            if (nextStation.isPresent()) {
                stations.add(nextStation.get().getPreviousStation());
                station = nextStation.get().getNextStation();
            }
            if (nextStation.isEmpty()) {
                station = null;
            }
        }

        return stations;
    }
}
