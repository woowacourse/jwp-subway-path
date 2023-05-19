package subway.line.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import subway.line.Line;
import subway.line.domain.section.application.ShortestPathResponse;
import subway.line.domain.section.application.SectionDao;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.station.application.StationDao;
import subway.line.domain.section.Section;
import subway.line.domain.station.Station;
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
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final SectionService sectionService;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao, SectionService sectionService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.sectionService = sectionService;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(request.getName(), request.getColor());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor(), sectionService.findAllStationsOrderByUp(line)))
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        List<Station> stations = sectionService.findAllStationsOrderByUp(persistLine);
        return LineResponse.of(persistLine, stations);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

    public ShortestPathResponse findShortestPath(String startingStationName, String destinationStationName) {
        final var startingStation = stationDao.findByName(startingStationName);
        final var destinationStation = stationDao.findByName(destinationStationName);
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
        for (Line line : lineDao.findAll()) {
            saveLineInGraph(graph, line);
        }
        return graph;
    }

    private void saveLineInGraph(WeightedMultigraph<Station, DefaultWeightedEdge> graph, Line line) {
        final var sections = sectionDao.findAllByLine(line);
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
}
