package subway.line.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import subway.common.exception.ExceptionMessages;
import subway.line.Line;
import subway.line.application.dto.LineSavingInfo;
import subway.line.application.dto.LineUpdatingInfo;
import subway.line.application.strategy.sectionsaving.SectionSavingStrategy;
import subway.line.application.strategy.stationdeleting.StationDeletingStrategy;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.application.ShortestPathResponse;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.station.Station;

import java.math.BigDecimal;
import java.util.List;

@Service
public class LineService {
    public static final int DEFAULT_FARE = 1250;
    public static final int MIN_5KM_FARE_DISTANCE = 10;
    public static final int MAX_5KM_FARE_DISTANCE = 50;
    public static final int MIN_8KM_FARE_DISTANCE = 51;
    public static final int SURCHARGE = 100;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final List<SectionSavingStrategy> sectionSavingStrategies;
    private final List<StationDeletingStrategy> stationDeletingStrategies;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, List<SectionSavingStrategy> sectionSavingStrategies, List<StationDeletingStrategy> stationDeletingStrategies) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.sectionSavingStrategies = sectionSavingStrategies;
        this.stationDeletingStrategies = stationDeletingStrategies;
    }

    public Line saveLine(LineSavingInfo savingInfo) {
        return lineRepository.makeLine(savingInfo.getName(), savingInfo.getColor());
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id);
    }

    public void updateLine(LineUpdatingInfo updatingInfo) {
        final var line = lineRepository.findById(updatingInfo.getLineId());
        line.changeName(updatingInfo.getName());
        line.changeColor(updatingInfo.getColor());
        lineRepository.update(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public ShortestPathResponse findShortestPath(Station startingStation, Station destinationStation) {
        final var graph = makeGraph();
        final var shortestPath = new DijkstraShortestPath<>(graph)
                .getPath(startingStation, destinationStation);
        return new ShortestPathResponse(startingStation, destinationStation, shortestPath.getVertexList(), shortestPath.getWeight());
    }

    private WeightedMultigraph<Station, DefaultWeightedEdge> makeGraph() {
        final var graph = new WeightedMultigraph<Station, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        for (Line line : lineRepository.findAll()) {
            saveLineInGraph(graph, line);
        }
        return graph;
    }

    public long saveSection(Line line, Station previousStation, Station nextStation, Distance distance) {
        for (SectionSavingStrategy strategy : sectionSavingStrategies) {
            if (strategy.support(line, previousStation, nextStation, distance)) {
                return strategy.insert(line, previousStation, nextStation, distance);
            }
        }
        throw new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED);
    }

    private void saveLineInGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        final var sections = sectionRepository.findAllByLineId(line.getId());
        for (Section section : sections) {
            saveSectionInGraph(graph, section);
        }
    }

    private static void saveSectionInGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Section section) {
        graph.addVertex(section.getPreviousStation());
        graph.addVertex(section.getNextStation());
        if (section.isNextStationEmpty()) return;
        graph.setEdgeWeight(
                graph.addEdge(section.getPreviousStation(), section.getNextStation()),
                section.getDistance().getValue()
        );
    }

    public BigDecimal calculateFare(double km) {
        var fare = new BigDecimal(DEFAULT_FARE);
        if (MIN_5KM_FARE_DISTANCE <= km && km <= MAX_5KM_FARE_DISTANCE) {
            fare = calculate5kmFare(km, fare);
        }
        if (MIN_8KM_FARE_DISTANCE <= km) {
            fare = calculate8kmFare(km, fare);
        }
        return fare;
    }

    private static BigDecimal calculate5kmFare(double km, BigDecimal fare) {
        return fare.add(new BigDecimal(SURCHARGE * Math.floor((km - 10) / 5)));
    }

    private static BigDecimal calculate8kmFare(double km, BigDecimal fare) {
        return fare.add(new BigDecimal(SURCHARGE * Math.floor((km - 10) / 8)));
    }

    public void deleteStation(Line line, Station station) {
        for (StationDeletingStrategy strategy : stationDeletingStrategies) {
            if (strategy.support(line, station)) {
                strategy.deleteStation(line, station);
                return;
            }
        }
        throw new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED);
    }
}
