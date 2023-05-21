package subway.application;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.StationEntity;
import subway.dao.vo.SectionStationMapper;
import subway.domain.*;
import subway.dto.request.PathRequest;
import subway.dto.response.PathResponse;
import subway.exception.NotFoundException;

import java.util.List;

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

        Map map = getMap();
        Path path = map.getShortestPath(startStation, endStation);
        Fare fare = Fare.createByDistance(path.getDistance());
        return new PathResponse(path.getPath(), path.getDistance(), fare.getFare());
    }

    private Station getStationById(Long id) {
        StationEntity stationEntity = stationDao.findById(id)
                                                .orElseThrow(() -> new NotFoundException("해당 역을 찾을 수 없습니다."));
        return Station.from(stationEntity);
    }

    private Map getMap() {
        WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        addVertex(stationGraph);
        addEdgeWight(stationGraph);

        return new Map(stationGraph);
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
