package subway.application;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import subway.ui.dto.request.PathRequest;
import subway.ui.dto.request.SectionCreateRequest;
import subway.ui.dto.request.SectionDeleteRequest;
import subway.ui.dto.response.PathResponse;
import subway.ui.dto.response.StationInfo;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionService(
            final LineRepository lineRepository,
            final SectionRepository sectionRepository,
            final StationRepository stationRepository
    ) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void createSection(SectionCreateRequest request) {
        Long lineId = request.getLineId();
        Line line = findLine(lineId);

        Station leftStation = findStation(request.getLeftStationName());
        Station rightStation = findStation(request.getRightStationName());
        Distance distance = new Distance(request.getDistance());
        Section section = new Section(leftStation, rightStation, distance);

        line.addSection(section);
        sectionRepository.deleteAllByLineId(lineId);
        sectionRepository.saveAllByLineId(lineId, line.getSections());
    }

    public PathResponse findPath(PathRequest pathRequest) {
        String fromStationName = pathRequest.getFromStationName();
        String toStationName = pathRequest.getToStationName();
        Station fromStation = findStation(fromStationName);
        Station toStation = findStation(toStationName);

        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        List<Station> stations = stationRepository.findAll();
        for (final Station station : stations) {
            graph.addVertex(station);
        }

        List<LinkedList<Section>> allSections = lineRepository.findAll()
                .stream()
                .map(Line::getSections)
                .collect(Collectors.toList());
        for (final LinkedList<Section> sections : allSections) {
            for (final Section section : sections) {
                graph.setEdgeWeight(graph.addEdge(section.getLeft(), section.getRight()), section.getDistance());
            }
        }

        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        int pathDistance = (int) dijkstraShortestPath.getPathWeight(fromStation, toStation);
        List<StationInfo> stationInfos = dijkstraShortestPath.getPath(fromStation, toStation)
                .getVertexList()
                .stream()
                .map(station -> new StationInfo(station.getName()))
                .collect(Collectors.toList());

        return new PathResponse(pathDistance, stationInfos);
    }

    @Transactional
    public void deleteSection(SectionDeleteRequest request) {
        Long lineId = request.getLineId();
        Line line = findLine(lineId);
        Station station = findStation(request.getStationName());

        line.deleteSection(station);
        sectionRepository.deleteAllByLineId(lineId);
        sectionRepository.saveAllByLineId(lineId, line.getSections());
    }

    private Line findLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
    }

    private Station findStation(String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));
    }
}
