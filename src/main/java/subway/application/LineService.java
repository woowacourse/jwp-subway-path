package subway.application;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.PathDao;
import subway.dao.entity.PathEntity;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.exception.LineNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineDao lineDao;
    private final PathDao pathDao;

    public LineService(LineDao lineDao, final PathDao pathDao) {
        this.lineDao = lineDao;
        this.pathDao = pathDao;
    }

    @Transactional
    public Long saveLine(LineRequest request) {
        try {
            Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
            return persistLine.getId();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("노선 이름은 중복될 수 없습니다.");
        }
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public List<Station> findAllStations(final Long lineId) {
        final Line line = lineDao.findById(lineId).orElseThrow(LineNotFoundException::new);

        final List<PathEntity> pathEntities = pathDao.findStationsByLineId(lineId);
        if (pathEntities.isEmpty()) {
            return Collections.emptyList();
        }

        return makeStationsFromEntities(pathEntities, line);
    }

    private List<Station> makeStationsFromEntities(final List<PathEntity> pathEntities, final Line line) {
        final Station startStation = findStartStation(pathEntities);

        return Stream.iterate(startStation, beforeStation -> findNextStation(pathEntities, beforeStation, line))
                .limit(pathEntities.size())
                .collect(Collectors.toUnmodifiableList());
    }

    private Station findStartStation(final List<PathEntity> pathEntities) {
        return pathEntities.stream()
                .filter(pathEntity -> pathEntity.getDistance() == null)
                .findAny()
                .map(pathEntity -> new Station(pathEntity.getStationId(), pathEntity.getName()))
                .get();
    }

    private Station findNextStation(final List<PathEntity> pathEntities, Station beforeStation, final Line line) {
        final PathEntity nextPath = findNextPath(pathEntities, beforeStation);
        Station next = new Station(nextPath.getStationId(), nextPath.getName());

        beforeStation = beforeStation.addDownStation(line, new Path(next, nextPath.getDistance()));
        next = next.addUpStation(line, new Path(beforeStation, nextPath.getDistance()));

        return next;
    }

    private PathEntity findNextPath(final List<PathEntity> pathEntities, final Station beforeStation) {
        return pathEntities.stream()
                .filter(pathEntity -> Objects.equals(pathEntity.getUpStationId(), beforeStation.getId()))
                .findAny()
                .get();
    }
}
