package subway.line.domain.section.application;

import org.springframework.stereotype.Service;
import subway.common.exception.ExceptionMessages;
import subway.line.domain.section.application.exception.StationNotConnectedException;
import subway.line.domain.section.application.exception.StationNotRegisteredException;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.strategy.SectionInsertionStrategy;
import subway.line.application.LineDao;
import subway.line.domain.station.application.StationDao;
import subway.line.Line;
import subway.line.domain.station.Station;

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

    public Distance findDistanceBetween(Station stationA, Station stationB, Line line) {
        Optional<Section> subwayMapOptional = sectionDao.findByPreviousStation(stationA, line);
        if (subwayMapOptional.isPresent() && subwayMapOptional.get().getNextStation().equals(stationB)) {
            return subwayMapOptional.get().getDistance();
        }

        Optional<Section> subwayMapOptional1 = sectionDao.findByNextStation(stationA, line);
        if (subwayMapOptional1.isPresent() && subwayMapOptional1.get().getPreviousStation().equals(stationB)) {
            return subwayMapOptional1.get().getDistance();
        }

        throw new StationNotConnectedException(ExceptionMessages.STATION_NOT_CONNECTED);
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
