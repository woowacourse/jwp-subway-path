package subway.line.application;

import org.springframework.stereotype.Service;
import subway.line.Line;
import subway.line.application.dto.CustomerConditionInfo;
import subway.line.application.dto.LineUpdatingInfo;
import subway.line.application.exception.NotRegisteredLineException;
import subway.line.application.exception.SectionDeletingNotPossibleException;
import subway.line.application.exception.SectionSavingNotPossibleException;
import subway.line.application.strategy.sectionsaving.SectionSavingStrategy;
import subway.line.application.strategy.stationdeleting.StationDeletingStrategy;
import subway.line.domain.fare.Fare;
import subway.line.domain.fare.application.SubwayFareMeter;
import subway.line.domain.fare.application.SurchargeService;
import subway.line.domain.fare.dto.CustomerCondition;
import subway.line.domain.navigation.application.NavigationService;
import subway.line.domain.navigation.application.exception.NavigationNotFoundException;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;
    private final SectionService sectionService;
    private final NavigationService navigationService;
    private final SubwayFareMeter subwayFareMeter;
    private final SurchargeService surchargeService;
    private final List<SectionSavingStrategy> sectionSavingStrategies;
    private final List<StationDeletingStrategy> stationDeletingStrategies;

    public LineService(LineRepository lineRepository, SectionService sectionService, NavigationService navigationService, SubwayFareMeter subwayFareMeter, SurchargeService surchargeService, List<SectionSavingStrategy> sectionSavingStrategies, List<StationDeletingStrategy> stationDeletingStrategies) {
        this.lineRepository = lineRepository;
        this.sectionService = sectionService;
        this.navigationService = navigationService;
        this.subwayFareMeter = subwayFareMeter;
        this.surchargeService = surchargeService;
        this.sectionSavingStrategies = sectionSavingStrategies;
        this.stationDeletingStrategies = stationDeletingStrategies;
    }

    public Line saveLine(Line line) {
        return lineRepository.save(line);
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id);
    }

    public void updateLine(Line line, LineUpdatingInfo updatingInfo) {
        line.changeName(updatingInfo.getName());
        line.changeColor(updatingInfo.getColor());
        lineRepository.update(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public long saveSection(Line line, Station previousStation, Station nextStation, Distance distance) {
        for (SectionSavingStrategy strategy : sectionSavingStrategies) {
            if (strategy.support(line, previousStation, nextStation, distance)) {
                return strategy.insert(line, previousStation, nextStation, distance);
            }
        }
        throw new SectionSavingNotPossibleException();
    }

    public void deleteStation(Line line, Station station) {
        for (StationDeletingStrategy strategy : stationDeletingStrategies) {
            if (strategy.support(line, station)) {
                strategy.deleteStation(line, station);
                return;
            }
        }
        throw new SectionDeletingNotPossibleException();
    }

    public List<Station> findShortestPath(Station stationA, Station stationB) {
        try {
            return navigationService.findShortestPath(stationA, stationB);
        } catch (NavigationNotFoundException e) {
            updateNavigation();
            return navigationService.findShortestPath(stationA, stationB);
        }
    }

    public Distance findShortestDistance(Station stationA, Station stationB) {
        try {
            return navigationService.findShortestDistance(stationA, stationB);
        } catch (NavigationNotFoundException e) {
            updateNavigation();
            return navigationService.findShortestDistance(stationA, stationB);
        }
    }

    private void updateNavigation() {
        final var lines = lineRepository.findAll();
        final var sectionsOfAllLines = lines.stream()
                .map(Line::getSections)
                .collect(Collectors.toList());
        navigationService.updateNavigation(sectionsOfAllLines);
    }

    public Fare calculateFare(Station startingStation, Station destinationStation, CustomerConditionInfo customerConditionInfo) {
        final var distance = findShortestDistance(startingStation, destinationStation);
        final var path = findShortestPath(startingStation, destinationStation);
        final var surcharges = surchargeService.getSurcharges(path);

        final var customerCondition = new CustomerCondition(distance, surcharges, customerConditionInfo.getAge());
        return subwayFareMeter.calculateFare(customerCondition);
    }

    public Fare saveSurcharge(long lineId, Fare surcharge) {
        final var line = lineRepository.findById(lineId);
        if (line != null) {
            return surchargeService.saveSurcharge(lineId, surcharge);
        }
        throw new NotRegisteredLineException();
    }
}
