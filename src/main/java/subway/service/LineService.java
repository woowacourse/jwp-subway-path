package subway.service;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.edge.Distance;
import subway.domain.edge.Edge;
import subway.domain.edge.Edges;
import subway.domain.graph.SubwayGraph;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.exception.LineAlreadyExistException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.EdgeRepository;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.ui.line.dto.AddStationToLineRequest;
import subway.ui.line.dto.LineCreateRequest;
import subway.ui.line.dto.ShortestPathResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final EdgeRepository edgeRepository;

    public LineService(final LineRepository lineRepository, final StationRepository stationRepository, final EdgeRepository edgeRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.edgeRepository = edgeRepository;
    }

    @Transactional
    public Line createNewLine(final LineCreateRequest request) {
        final Station upStation = stationRepository.save(new Station(request.getUpStation()));
        final Station downStation = stationRepository.save(new Station(request.getDownStation()));
        final Line line = new Line(request.getLineName());
        if (lineRepository.findLineByName(line).isPresent()) {
            throw new LineAlreadyExistException(request.getLineName());
        }
        final Edge edge = new Edge(upStation, downStation, new Distance(request.getDistance()));

        return lineRepository.saveWithEdges(new Line(line.getName(), new Edges(List.of(edge))));
    }

    @Transactional
    public Line addStationToLine(final Long lineId, final AddStationToLineRequest request) {
        final Station existStation = stationRepository.findById(request.getExistStationId())
                .orElseThrow(() -> new StationNotFoundException(request.getExistStationId()));
        final Station newStation = stationRepository.save(new Station(request.getNewStationName()));
        final Line line = lineRepository.findLineWithEdgesByLineId(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));

        line.addEdge(existStation, newStation, request.getDirection().getDirectionStrategy(), new Distance(request.getDistance()));
        edgeRepository.deleteAllByLineId(lineId);
        edgeRepository.insertAllByLineId(lineId, line.getEdges());

        return line;
    }

    public Line findLineById(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }

    public List<Station> getStations(final Long lineId) {
        final Line findLine = lineRepository.findLineWithEdgesByLineId(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));

        return findLine.stations();
    }

    public List<Line> findAllLines() {

        return lineRepository.findAllWithEdges();
    }

    @Transactional
    public void deleteStationFromLine(final Long lineId, final Long stationId) {
        final Line line = lineRepository.findLineWithEdgesByLineId(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
        final Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new StationNotFoundException(stationId));

        line.delete(station);
        edgeRepository.deleteAllByLineId(lineId);
        edgeRepository.insertAllByLineId(lineId, line.getEdges());
    }

    public ShortestPathResponse getShortestPath(final Long fromId, final Long toId) {
        final Station fromStation = stationRepository.findById(fromId)
                .orElseThrow(() -> new StationNotFoundException(fromId));
        final Station toStation = stationRepository.findById(toId)
                .orElseThrow(() -> new StationNotFoundException(toId));

        final List<Line> lines = lineRepository.findAllWithEdges();


        final DijkstraShortestPath<Station, Edge> shortestPath = SubwayGraph.getShortestPath(lines);
        final double pathWeight = shortestPath.getPathWeight(fromStation, toStation);
        final List<Station> stations = shortestPath.getPath(fromStation, toStation).getVertexList();

        return new ShortestPathResponse(pathWeight, stations);
    }
}
