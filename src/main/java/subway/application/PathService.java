package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayGraph;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.entity.LineStationEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public ShortestPathResponse findShortestPath(final ShortestPathRequest shortestPathRequest) {
        SubwayGraph subwayGraph = new SubwayGraph();
        List<Section> sections = toDomain(sectionDao.findLineStationById());
        Map<Long, Station> stationMap = makeStationMap(stationDao.findAll());

        subwayGraph.makeGraph(sections);
        Station upStation = stationMap.get(shortestPathRequest.getUpStationId());
        Station downStation = stationMap.get(shortestPathRequest.getDownStationId());
        List<String> dijkstraShortestPath = subwayGraph.getDijkstraShortestPath(upStation, downStation);
        double shortestPathWeight = subwayGraph.getShortestPathWeight(upStation, downStation);
        return new ShortestPathResponse(dijkstraShortestPath, shortestPathWeight);
    }

    private List<Section> toDomain(final List<LineStationEntity> lineStationEntities) {
        return lineStationEntities.stream()
                .map(lineStationEntity -> {
                    StationEntity upStationEntity = lineStationEntity.getUpStationEntity();
                    StationEntity downStationEntity = lineStationEntity.getDownStationEntity();
                    SectionEntity sectionEntity = lineStationEntity.getSectionEntity();
                    Station upStation = new Station(upStationEntity.getId(), upStationEntity.getName());
                    Station downStation = new Station(downStationEntity.getId(), downStationEntity.getName());
                    return new Section(sectionEntity.getId(), upStation, downStation, sectionEntity.getDistance(), sectionEntity.getOrder());
                }).collect(Collectors.toList());
    }

    private Map<Long, Station> makeStationMap(final List<StationEntity> stationDao) {
        return stationDao.stream()
                .collect(Collectors.toMap(
                        StationEntity::getId,
                        station -> new Station(station.getId(), station.getName())
                ));
    }
}
