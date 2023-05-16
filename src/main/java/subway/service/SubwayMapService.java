package subway.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.subway.*;
import subway.dto.route.RouteShortCutRequest;
import subway.dto.route.RouteShortCutResponse;
import subway.dto.station.LineMapResponse;
import subway.dto.station.StationResponse;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubwayMapService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;

    public SubwayMapService(final SectionRepository sectionRepository, final LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public LineMapResponse showLineMapByLineNumber(final Long lineNumber) {
        Sections sections = sectionRepository.findSectionsByLineNumber(lineNumber);
        LineMap lineMap = LineMap.from(sections);

        return lineMap.getOrderedStations(sections).stream()
                .map(StationResponse::from)
                .collect(Collectors.collectingAndThen(Collectors.toList(), LineMapResponse::from));
    }

    @Transactional(readOnly = true)
    public RouteShortCutResponse findShortCutRoute(final RouteShortCutRequest req) {
        // 1. 데이터셋
        List<Line> lines = makeAllLines();


        // 2. 라이브러리로 최단 경로 조회
        Map<String, Set<String>> lineMapInPath = new LinkedHashMap<>();
        Map<String, Long> stationWithIdMap = new LinkedHashMap<>();

        WeightedMultigraph<String, DefaultWeightedEdge> graph = findShortestPathGraph(lines, lineMapInPath, stationWithIdMap);


        // 3. 데이터 메이킹 ()
        DijkstraShortestPath shortestPath = new DijkstraShortestPath(graph);
        GraphPath path = shortestPath.getPath(req.getStart(), req.getDestination());

        // 4. 데이터 후처리
        List<String> shortestPathList = path.getVertexList();
        List<String> shortestPathWithLineNames = makeShortestPathWithLineName(lineMapInPath, shortestPathList);
        int distance = (int) path.getWeight();
        int fee = calculateFee(distance);


        List<StationResponse> shortestPathResponse = shortestPathList.stream()
                .map(station -> StationResponse.from(new Station(stationWithIdMap.get(station), station)))
                .collect(Collectors.toList());

        List<StationResponse> shortestPathWithLineResponse = shortestPathWithLineNames.stream()
                .map(station -> StationResponse.from(new Station(station)))
                .collect(Collectors.toList());

        return RouteShortCutResponse.from(shortestPathResponse, shortestPathWithLineResponse, fee, distance);
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> findShortestPathGraph(final List<Line> lines, final Map<String, Set<String>> lineMapInPath, final Map<String, Long> stationWithIdMap) {
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        for (Line line : lines) {
            for (Section section : line.getSections()) {
                String upStation = section.getUpStation().getName();
                String downStation = section.getDownStation().getName();

                stationWithIdMap.put(upStation, section.getUpStation().getId());
                stationWithIdMap.put(downStation, section.getDownStation().getId());

                graph.addVertex(upStation);
                graph.addVertex(downStation);
                graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());

                lineMapInPath.computeIfAbsent(line.getName(), station -> new LinkedHashSet<>()).
                        addAll(Arrays.asList(upStation, downStation));
            }
        }
        return graph;

    }

    private List<String> makeShortestPathWithLineName(final Map<String, Set<String>> lineMapInPath, final List<String> shortestPathList) {
        return shortestPathList.stream()
                .map(station -> {
                    List<String> lineNames = lineMapInPath.entrySet().stream()
                            .filter(entry -> entry.getValue().contains(station))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());

                    return station + " (" + String.join(", ", lineNames) + ")";
                })
                .collect(Collectors.toList());
    }


    private List<Line> makeAllLines() {
        return lineRepository.findAll().stream()
                .map(lineEntity -> {
                    Sections sections = sectionRepository.findSectionsByLineNumber(lineEntity.getLineNumber());
                    return lineRepository.findByLineNameAndSections(lineEntity.getName(), sections);
                })
                .collect(Collectors.toList());
    }

    private int calculateFee(final int distance) {
        int fee = 1250;
        if (distance <= 10) {
            return fee;
        }

        fee += (int) ((Math.ceil((distance - 10) / 5) + 1) * 100);
        if (distance < 50) {
            return fee;
        }

        fee += (int) ((Math.ceil((distance - 50) / 8) + 1) * 100);
        return fee;
    }
}
