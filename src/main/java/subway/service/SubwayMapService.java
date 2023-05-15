package subway.service;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.subway.Line;
import subway.domain.subway.LineMap;
import subway.domain.subway.Section;
import subway.domain.subway.Sections;
import subway.dto.route.RouteShortCutRequest;
import subway.dto.route.RouteShortCutResponse;
import subway.dto.station.LineMapResponse;
import subway.dto.station.StationResponse;
import subway.entity.LineEntity;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
        List<LineEntity> lineEntities = lineRepository.findAll();

        List<Sections> sectionsList = lineEntities.stream()
                .map(i -> sectionRepository.findSectionsByLineNumber(i.getLineNumber()))
                .collect(Collectors.toList());

        List<Line> lines = lineEntities.stream()
                .flatMap(lineEntity -> sectionsList.stream()
                        .map(sections -> lineRepository.findByLineNameAndSections(lineEntity.getName(), sections)))
                .collect(Collectors.toList());

        // 2. 라이브러리 호출
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);

        // 추가
        Map<String, List<String>> map = new LinkedHashMap<>();

        for (Line line : lines) {
            Sections sections = line.getSections();
            for (Section section : sections.getSections()) {
                String upStation = section.getUpStation().getName();
                String downStation = section.getDownStation().getName();

                graph.addVertex(upStation);
                graph.addVertex(downStation);

                graph.setEdgeWeight(graph.addEdge(upStation, downStation), section.getDistance());
            }
        }

        // 3. 데이터 메이킹
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);

        GraphPath path = dijkstraShortestPath.getPath(req.getStart(), req.getDestination());


        List<String> shortestPath = path.getVertexList();
        int distance = (int) path.getWeight();

        System.out.println("shortRoute: " + shortestPath + " distance: " + distance);
        System.out.println("map = " + map);

        return null;
    }
}
