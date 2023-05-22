package subway.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShortestPathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public ShortestPathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public ShortestPathResponse getDijkstraShortestPath(final ShortestPathRequest shortestPathRequest) {
        Distance allDistance = new Distance(0);

        WeightedMultigraph<String, DefaultWeightedEdge> graph
                = new WeightedMultigraph(DefaultWeightedEdge.class);
        List<Section> allSections = sectionDao.findAll().stream()
                .map(sectionStationResultMap -> Section.of(sectionStationResultMap))
                .collect(Collectors.toList());

        List<Station> stations = stationDao.findAll();
        for (Station station : stations) {
            graph.addVertex(station.getName());
        }
        for (Section section : allSections) {
            allDistance.plusDistance(section.getDistance());
            graph.setEdgeWeight(graph.addEdge(section.getUpStation().getName(), section.getDownStation().getName()), section.getDistance());
        }

        DijkstraShortestPath dijkstraShortestPathTest = new DijkstraShortestPath(graph);
        List<String> shortestPath = dijkstraShortestPathTest.getPath(shortestPathRequest.getStartStationName(), shortestPathRequest.getTargetStationName()).getVertexList();

        return new ShortestPathResponse(shortestPath, calculate(allDistance));
    }

    private int calculate(Distance distance) {
        if (distance.isShorterSame(10)) {
            return 1250;
        }

        if (distance.isShorterSame(50)) {
            return 1250 + over10under50(distance.minusDistance(10).getDistance());
        }
        return 1250 + over10under50(40) + over50(distance.minusDistance(50).getDistance());
    }

    private int over10under50(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int over50(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
