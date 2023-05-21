package subway.application;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.dao.vo.SectionStationMapper;
import subway.domain.Path;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        Station startStation = getStationById(pathRequest.getStartStationId());
        Station endStation = getStationById(pathRequest.getEndStationId());

        Path path = getPath();
        GraphPath<Station, DefaultWeightedEdge> pathGraph = path.getShortestPath(startStation, endStation);
        List<String> stations = getStations(pathGraph);
        int distance = (int) pathGraph.getWeight();
        return new PathResponse(stations, distance);
    }

    private List<String> getStations(GraphPath<Station, DefaultWeightedEdge> pathGraph) {
        List<Station> stations = pathGraph.getVertexList();
        return stations.stream()
                       .map(Station::getName)
                       .collect(Collectors.toList());
    }

    private Station getStationById(Long id) {
        StationEntity stationEntity = stationDao.findById(id)
                                                .orElseThrow(() -> new NotFoundException("해당 역을 찾을 수 없습니다."));
        return Station.from(stationEntity);
    }

    private Path getPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph = new WeightedMultigraph(DefaultWeightedEdge.class);

        addVertex(stationGraph);
        addEdgeWight(stationGraph);

        return new Path(stationGraph);
    }

    private void addVertex(WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph) {
        List<StationEntity> stations = stationDao.findAll();
        for (StationEntity stationEntity : stations) {
            Station station = Station.from(stationEntity);
            stationGraph.addVertex(station);
        }
    }

    private void addEdgeWight(WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph) {
        List<SectionStationMapper> sections = sectionDao.findAll();
        for (SectionStationMapper sectionEntity : sections) {
            Section section = Section.from(sectionEntity);
            DefaultWeightedEdge sectionEdge = stationGraph.addEdge(section.getUpStation(), section.getDownStation());
            stationGraph.setEdgeWeight(sectionEdge, section.getDistance());
        }
    }
}
