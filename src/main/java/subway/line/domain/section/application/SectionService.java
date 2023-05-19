package subway.line.domain.section.application;

import org.springframework.stereotype.Service;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.application.LineRepository;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.exception.StationNotConnectedException;
import subway.line.domain.section.application.strategy.SectionInsertionStrategy;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;
import subway.line.domain.station.application.StationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final List<SectionInsertionStrategy> strategies;

    public SectionService(SectionRepository sectionRepository, StationRepository stationRepository, LineRepository lineRepository, List<SectionInsertionStrategy> strategies) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.strategies = strategies;
    }

    public long insert(long lineId, String previousStationName, String nextStationName, Distance distance, boolean isDown) {
        Line line = lineRepository.findById(lineId);
        Station previousStation = stationRepository.findByName(previousStationName);
        Station nextStation = stationRepository.findByName(nextStationName);

        if (isDown) {
            return insert(line, previousStation, nextStation, distance);
        }
        return insert(line, nextStation, previousStation, distance);
    }

    private long insert(Line line, Station previousStation, Station nextStation, Distance distance) {
        for (SectionInsertionStrategy strategy : strategies) {
            if (strategy.support(line, previousStation, nextStation, distance)) {
                return strategy.insert(line, previousStation, nextStation, distance);
            }
        }
        throw new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED);
    }

    public Distance findDistanceBetween(Station stationA, Station stationB, Line line) {
        Optional<Section> subwayMapOptional = sectionRepository.findByPreviousStation(stationA, line);
        if (subwayMapOptional.isPresent() && subwayMapOptional.get().getNextStation().equals(stationB)) {
            return subwayMapOptional.get().getDistance();
        }

        Optional<Section> subwayMapOptional1 = sectionRepository.findByNextStation(stationA, line);
        if (subwayMapOptional1.isPresent() && subwayMapOptional1.get().getPreviousStation().equals(stationB)) {
            return subwayMapOptional1.get().getDistance();
        }

        throw new StationNotConnectedException(ExceptionMessages.STATION_NOT_CONNECTED);
    }

    public List<Station> findAllStationsOrderByUp(Line line) {
        final var stations = new ArrayList<Station>();
        final var sections = sectionRepository.findAllByLine(line);

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
