package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.Station;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.PathDao;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.PathEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        final Map<Long, String> mapper = new HashMap<>();
        stations.forEach(station -> mapper.put(station.getId(), station.getName()));
        final Map<Station, Path> paths = pathEntities.stream()
                .collect(
                        toMap(pathEntity -> new Station(pathEntity.getUpStationId(), mapper.get(pathEntity.getUpStationId())),
                                pathEntity -> new Path(
                                        new Station(pathEntity.getDownStationId(), mapper.get(pathEntity.getDownStationId())),
                                        pathEntity.getDistance()))
                );
        return line.setPath(paths);
    }
}
