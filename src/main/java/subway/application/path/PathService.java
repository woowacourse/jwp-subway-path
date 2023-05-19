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
import subway.dto.path.PathRequest;
import subway.dto.path.PathResponse;
import subway.exception.path.IllegalPathException;

@Service
@Transactional
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final PricePolicy pricePolicy;

    public PathService(StationDao stationDao, SectionDao sectionDao, PricePolicy pricePolicy) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.pricePolicy = pricePolicy;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(PathRequest pathRequest) {
        validateSameStation(pathRequest);
        Station originStation = new Station(pathRequest.getOriginStationName());
        Station destinationStation = new Station(pathRequest.getDestinationStationName());
        validateStationInSection(originStation, destinationStation);
        Path path = getPath(originStation, destinationStation);
        List<String> stations = path.getStations();
        int distance = path.getTotalDistance();
        return new PathResponse(stations, distance, path.getPrice(pricePolicy));
    }

    private void validateSameStation(PathRequest request) {
        if (Objects.equals(request.getOriginStationName(), request.getDestinationStationName())) {
            throw new IllegalPathException("조회할 역이 서로 같습니다.");
        }
    }

    private void validateStationInSection(Station originStation, Station destinationStation) {
        if (sectionDao.doesNotExistByStationName(originStation.getName()) || sectionDao.doesNotExistByStationName(
                destinationStation.getName())) {
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
