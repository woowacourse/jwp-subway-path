package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.Station;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.PathDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.PathEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Repository
public class SubwayRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final PathDao pathDao;

    public SubwayRepository(final LineDao lineDao, final StationDao stationDao, final PathDao pathDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.pathDao = pathDao;
    }

    public Line findLine(final Long lineId) {
        final Line line = lineDao.findById(lineId);
        final List<PathEntity> pathEntities = pathDao.findAllByLineId(lineId);
        final List<Station> stations = stationDao.findAll();
        final Map<Long, String> mapper = stations.stream()
                .collect(toMap(Station::getId, Station::getName));
        final Map<Station, Path> paths = entitiesToPath(pathEntities, mapper);
        return line.setPath(paths);
    }

    private Map<Station, Path> entitiesToPath(final List<PathEntity> pathEntities, final Map<Long, String> mapper) {
        final Map<Station, Path> paths = pathEntities.stream()
                .collect(
                        toMap(pathEntity -> new Station(pathEntity.getUpStationId(), mapper.get(pathEntity.getUpStationId())),
                                pathEntity -> new Path(
                                        new Station(pathEntity.getDownStationId(), mapper.get(pathEntity.getDownStationId())),
                                        pathEntity.getDistance()))
                );
        return paths;
    }

    public List<Line> findLines() {
        final List<Line> lines = lineDao.findAll();
        final List<PathEntity> pathEntities = pathDao.findAll();
        final List<Station> stations = stationDao.findAll();
        final Map<Long, String> mapper = stations.stream()
                .collect(toMap(Station::getId, Station::getName));

        final Map<Long, List<PathEntity>> pathEntitiesByLineId = pathEntities.stream()
                .collect(groupingBy(PathEntity::getLineId));

        return lines.stream()
                .map(line -> line.setPath(
                        entitiesToPath(pathEntitiesByLineId.get(line.getId()), mapper))
                )
                .collect(Collectors.toList());
    }
}
