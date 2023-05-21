package subway.application.path;

import java.util.List;
import java.util.Objects;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.domain.path.Path;
import subway.domain.station.Station;
import subway.dto.path.PathResponse;
import subway.exception.path.IllegalPathException;

@Service
@Transactional
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final DistancePricePolicy distancePricePolicy;

    public PathService(StationDao stationDao, SectionDao sectionDao, DistancePricePolicy distancePricePolicy) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.distancePricePolicy = distancePricePolicy;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(String originStationName, String destinationStationName) {
        validateSameStation(originStationName, destinationStationName);
        validateStationInSection(originStationName, destinationStationName);
        Path path = getPath(new Station(originStationName), new Station(destinationStationName));
        List<String> stations = path.getStations();
        int distance = path.getTotalDistance();
        return new PathResponse(stations, distance, distancePricePolicy.calculate(distance));
    }

    private void validateSameStation(String originStationName, String destinationStationName) {
        if (Objects.equals(originStationName, destinationStationName)) {
            throw new IllegalPathException("조회할 역이 서로 같습니다.");
        }
    }

    private void validateStationInSection(String originStationName, String destinationStationName) {
        if (sectionDao.doesNotExistByStationName(originStationName) || sectionDao.doesNotExistByStationName(
                destinationStationName)) {
            throw new IllegalPathException("해당 역이 구간에 존재하지 않습니다.");
        }
    }

    private Path getPath(Station originStation, Station destinationStation) {
        Graph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        initialVertex(graph);
        initialEdge(graph);
        DijkstraShortestPath<Station, DefaultWeightedEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return new Path(shortestPath.getPath(originStation, destinationStation));
    }

    private void initialVertex(Graph<Station, DefaultWeightedEdge> graph) {
        List<Station> stations = stationDao.findAll();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void initialEdge(Graph<Station, DefaultWeightedEdge> graph) {
        List<SectionEntity> sections = sectionDao.findAll();
        for (SectionEntity section : sections) {
            DefaultWeightedEdge edge = graph.addEdge(section.getUpBoundStation(), section.getDownBoundStation());
            graph.setEdgeWeight(edge, section.getDistance());
        }
    }
}
