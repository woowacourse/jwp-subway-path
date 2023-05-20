package subway.line.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import subway.line.Line;
import subway.line.domain.section.Section;
import subway.line.domain.section.application.SectionRepository;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.section.application.ShortestPathResponse;
import subway.line.domain.station.Station;
import subway.line.domain.station.application.StationRepository;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {
    public static final int DEFAULT_FARE = 1250;
    public static final int MIN_5KM_FARE_DISTANCE = 10;
    public static final int MAX_5KM_FARE_DISTANCE = 50;
    public static final int MIN_8KM_FARE_DISTANCE = 51;
    public static final int SURCHARGE = 100;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final List<SectionSavingStrategy> sectionSavingStrategies;
    private final List<StationDeletingStrategy> stationDeletingStrategies;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository, List<SectionSavingStrategy> sectionSavingStrategies, List<StationDeletingStrategy> stationDeletingStrategies) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.sectionSavingStrategies = sectionSavingStrategies;
        this.stationDeletingStrategies = stationDeletingStrategies;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.insert(request.getName(), request.getColor());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor(), sectionService.findAllStationsOrderByUp(line)))
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineRepository.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        List<Station> stations = sectionService.findAllStationsOrderByUp(persistLine);
        return LineResponse.of(persistLine, stations);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineRepository.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public ShortestPathResponse findShortestPath(String startingStationName, String destinationStationName) {
        final var startingStation = stationRepository.findByName(startingStationName);
        final var destinationStation = stationRepository.findByName(destinationStationName);
        return findShortestPath(startingStation, destinationStation);
    }

    private ShortestPathResponse findShortestPath(Station startingStation, Station destinationStation) {
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

    private void saveLineInGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        final var sections = sectionRepository.findAllByLine(line);
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

    public void deleteStation(Line line, String stationName) {
        final var station = stationRepository.findByName(stationName);
        for (StationDeletingStrategy strategy : stationDeletingStrategies) {
            if (strategy.support(line, station)) {
                strategy.deleteStation(line, station);
                return;
            }
        }

        throw new IllegalStateException(ExceptionMessages.STRATEGY_MAPPING_FAILED);
    }
}
