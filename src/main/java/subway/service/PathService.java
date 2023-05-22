package subway.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Path;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(PathRequest pathRequest) {
        List<StationEntity> stationEntities = stationDao.findAll();
        List<SectionEntity> sectionEntities = sectionDao.findAll();

        List<Station> stations = stationEntities.stream()
                .map(entity -> new Station(entity.getId(), entity.getName()))
                .collect(Collectors.toList());

        Map<Long, String> stationMappingEntities = stationDao.findAll().stream()
                .collect(Collectors.toMap(
                        StationEntity::getId,
                        StationEntity::getName)
                );

        List<Section> sections = sectionEntities.stream()
                .map(entity -> new Section(
                        new Station(entity.getUpStationId(), stationMappingEntities.get(entity.getUpStationId())),
                        new Station(entity.getDownStationId(), stationMappingEntities.get(entity.getDownStationId())),
                        entity.getDistance()
                )).collect(Collectors.toList());

        Path path = Path.of(stations, sections);
        List<String> shortestPath = path.getDijkstraShortestPath(
                pathRequest.getSourceStation(),
                pathRequest.getTargetStation()
        );

        int shortestPathDistance = path.getDijkstraShortestPathDistance(pathRequest.getSourceStation(),
                pathRequest.getTargetStation());

        return new PathResponse(shortestPath, shortestPathDistance);
    }
}
