package subway.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.response.PathResponse;
import subway.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathServiceImpl implements PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathServiceImpl(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Override
    public PathResponse findShortest(long startId, long endId) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        List<Sections> allSections = sectionDao.findAllSections();

        addSectionsToGraph(graph, allSections);

        Station start = stationDao.findById(startId);
        Station end = stationDao.findById(endId);
        return new PathResponse(findPath(start, end, graph), findDistance(start, end, graph));
    }

    private List<StationResponse> findPath(Station start, Station end, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        List<Station> shortestPath = dijkstraShortestPath.getPath(start, end).getVertexList();

        return  shortestPath.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private double findDistance(Station start, Station end, WeightedMultigraph<Station, DefaultWeightedEdge> graph) {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        return dijkstraShortestPath.getPathWeight(start, end);
    }

    private void addSectionsToGraph(WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph, List<Sections> allSections) {
        for (Sections sections : allSections) {
            for (Section lineSection : sections.getSections()) {
                stationGraph.addVertex(lineSection.getUpStation());
                stationGraph.addVertex(lineSection.getDownStation());
                stationGraph.setEdgeWeight(stationGraph.addEdge(lineSection.getUpStation(), lineSection.getDownStation()), lineSection.getDistance());
            }
        }
    }
}
