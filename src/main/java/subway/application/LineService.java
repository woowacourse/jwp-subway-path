package subway.application;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.PathDao;
import subway.dao.StationDao;
import subway.dao.entity.PathEntity;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.StationRequest;
import subway.exception.LineNotFoundException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineDao lineDao;
    private final PathDao pathDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, final PathDao pathDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.pathDao = pathDao;
        this.stationDao = stationDao;
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

    @Transactional
    public Long addStation(final Long lineId, final StationRequest stationRequest) {
        final List<Station> stations = findAllStations(lineId);

        if (stations.isEmpty()) {
            return insertInitialStations(stationRequest);
        }

        final String upStationName = stationRequest.getUpStationName();
        final String downStationName = stationRequest.getDownStationName();

        final Set<String> stationNames = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toSet());

        validate(upStationName, downStationName, stationNames);

        final Line line = lineDao.findById(lineId).get();

        if (stationNames.contains(upStationName)) {
            final Station upStation = stations.stream()
                    .filter(station -> station.nameEquals(upStationName))
                    .findAny()
                    .get();

            final Optional<Path> nextDownPath = upStation.getNextDownPath(line);

            if (nextDownPath.isEmpty()) {
                final Station persisted = stationDao.insert(new Station(stationRequest.getDownStationName()));
                pathDao.save(new PathEntity(upStation.getId(), persisted.getId(), stationRequest.getDistance()));

                return persisted.getId();
            }

            final Path originalPath = nextDownPath.get();
            if (originalPath.getDistance() <= stationRequest.getDistance()) {
                throw new IllegalArgumentException("기존 두 역 사이의 거리보다 클 수 없습니다.");
            }

            final Station finalStation = originalPath.getNext();
            final Station middleStation = stationDao.insert(new Station(stationRequest.getDownStationName()));

            final PathEntity upPath = new PathEntity(upStation.getId(), middleStation.getId(), stationRequest.getDistance());
            final PathEntity downPath = new PathEntity(middleStation.getId(), finalStation.getId(), originalPath.getDistance() - stationRequest.getDistance());

            pathDao.deletePathByUpStationIdAndDownStationId(upStation.getId(), finalStation.getId());
            pathDao.save(upPath);
            pathDao.save(downPath);

            return middleStation.getId();
        }

        final Station downStation = stations.stream()
                .filter(station -> station.nameEquals(downStationName))
                .findAny()
                .get();

        final Optional<Path> nextUpPath = downStation.getNextUpPath(line);

        if (nextUpPath.isEmpty()) {
            final Station persisted = stationDao.insert(new Station(stationRequest.getUpStationName()));
            pathDao.save(new PathEntity(persisted.getId(), downStation.getId(), stationRequest.getDistance()));

            return persisted.getId();
        }

        final Path originalPath = nextUpPath.get();
        if (originalPath.getDistance() <= stationRequest.getDistance()) {
            throw new IllegalArgumentException("기존 두 역 사이의 거리보다 클 수 없습니다.");
        }

        final Station finalStation = originalPath.getNext();
        final Station middleStation = stationDao.insert(new Station(stationRequest.getUpStationName()));

        final PathEntity upPath = new PathEntity(finalStation.getId(), middleStation.getId(), originalPath.getDistance() - stationRequest.getDistance());
        final PathEntity downPath = new PathEntity(middleStation.getId(), downStation.getId(), stationRequest.getDistance());

        pathDao.deletePathByUpStationIdAndDownStationId(finalStation.getId(), downStation.getId());
        pathDao.save(upPath);
        pathDao.save(downPath);

        return middleStation.getId();
        /**
         * 노선이 존재하지 않을 경우(existById) x
         *
         * 노선이 빈 경우(countLineStations==0) x
         *  - upStation, downStation 둘다 만들어서 넣으면 됨
         *
         * upStation, downStation 전부 존재 x
         * - 예외 발생
         *
         * upStation, downStation 전부 존재하지 않음 x
         * - 예외 발생
         *
         * 삽입하려는 경로의 DownStation이 DB의 Station 중 존재한다
         * - 삽입하려는 DownStation이 DownStation으로서 존재하는 경로를 찾는다
         *  - 없다면 지금 삽입하려는 UpStation이 맨 앞에 넣는 경로를 하나 추가한다
         *  - 있다면 중간에 삽입한다
         * 삽입하려는 경로의 UpStation이 DB의 Station 중 존재한다
         * - 삽입하려는 UpStation이 UpStation으로서 존재하는 경로를 찾는다
         *  - 없다면 지금 삽입하려는 DownStation이 맨 뒤에 넣는 경로를 하나 추가한다 x
         *  - 있다면 중간에 삽입한다
         */
    }

    private Long insertInitialStations(final StationRequest stationRequest) {
        final List<Station> newStations = List.of(
                new Station(stationRequest.getUpStationName()),
                new Station(stationRequest.getDownStationName()));

        final List<Station> persisted = stationDao.insertAll(newStations);

        final Station upStation = persisted.get(0);
        final Station downStation = persisted.get(1);
        final PathEntity path = new PathEntity(upStation.getId(), downStation.getId(), stationRequest.getDistance());
        pathDao.save(path);

        return upStation.getId();
    }

    private void validate(final String upStationName, final String downStationName, final Set<String> stationNames) {
        if (stationNames.contains(upStationName) && stationNames.contains(downStationName)) {
            throw new IllegalArgumentException("이미 존재하는 경로입니다.");
        }
        if (!stationNames.contains(upStationName) && !stationNames.contains(downStationName)) {
            throw new IllegalArgumentException("기존 역들과 이어져야 합니다.");
        }
    }

    public void deleteStation(final Long lineId, final Long id) {
    }
}
