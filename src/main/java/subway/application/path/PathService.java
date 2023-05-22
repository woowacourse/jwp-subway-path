package subway.application.path;

import java.util.List;
import java.util.Objects;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.domain.path.LineEdge;
import subway.domain.path.Path;
import subway.domain.station.Station;
import subway.exception.path.IllegalPathException;

@Service
@Transactional
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional(readOnly = true)
    public Path findPath(String originStationName, String destinationStationName) {
        validateSameStation(originStationName, destinationStationName);
        validateStationInSection(originStationName, destinationStationName);
        return getPath(new Station(originStationName), new Station(destinationStationName));
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
        Graph<Station, LineEdge> graph = new WeightedMultigraph<>(LineEdge.class);
        initialVertex(graph);
        initialEdge(graph);
        DijkstraShortestPath<Station, LineEdge> shortestPath = new DijkstraShortestPath<>(graph);
        return Path.from(shortestPath.getPath(originStation, destinationStation));
    }

    private void initialVertex(Graph<Station, LineEdge> graph) {
        List<Station> stations = stationDao.findAll();
        for (Station station : stations) {
            graph.addVertex(station);
        }
    }

    private void initialEdge(Graph<Station, LineEdge> graph) {
        List<SectionEntity> sections = sectionDao.findAll();
        for (SectionEntity section : sections) {
            LineEdge lineEdge = new LineEdge(section.getLineId());
            graph.addEdge(section.getUpBoundStation(), section.getDownBoundStation(), lineEdge);
            graph.setEdgeWeight(lineEdge, section.getDistance());
        }
    }
}
