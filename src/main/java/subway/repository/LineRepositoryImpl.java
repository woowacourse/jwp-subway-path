package subway.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import subway.dao.InterStationDao;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.InterStation;
import subway.domain.Line;
import subway.domain.Station;
import subway.entity.InterStationEntity;
import subway.entity.LineEntity;
import subway.entity.StationEntity;
import subway.exception.BusinessException;

@RequiredArgsConstructor
@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final InterStationDao interStationDao;

    @Override
    public Line save(final Line line) {
        final LineEntity lineEntity = lineDao.insert(LineEntity.from(line));
        final List<InterStationEntity> interStations = lineEntity.getInterStationEntities();
        interStationDao.insertAll(interStations);
        return findByName(line.getName())
                .orElseThrow(BusinessException::new);
    }

    @Override
    public Line update(final Line line) {
        interStationDao.deleteAllByLineId(line.getId());
        final List<InterStationEntity> interStationEntities = line.getInterStations()
                .stream()
                .map(interStation -> InterStationEntity.of(interStation, line.getId()))
                .collect(Collectors.toUnmodifiableList());
        interStationDao.insertAll(interStationEntities);
        return findByName(line.getName())
                .orElseThrow(BusinessException::new);
    }

    @Override
    public Optional<Line> findByName(final String name) {
        final List<StationEntity> stationEntities = stationDao.findAll();
        final List<InterStationEntity> interStationEntities = interStationDao.findAll();
        return lineDao.findByName(name).map(
                lineEntity -> makeLine(lineEntity, stationEntities, interStationEntities)
        );
    }

    @Override
    public List<Line> findAll() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        final List<StationEntity> stationEntities = stationDao.findAll();
        final List<InterStationEntity> interStationEntities = interStationDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> makeLine(lineEntity, stationEntities, interStationEntities))
                .collect(Collectors.toList());
    }

    private Line makeLine(final LineEntity lineEntity, final List<StationEntity> stationEntities,
            final List<InterStationEntity> interStationEntities) {
        final List<InterStation> interStations = interStationEntities.stream()
                .filter(interStationEntity -> interStationEntity.getLineId().equals(lineEntity.getId()))
                .map(interStationEntity -> makeInterStation(interStationEntity, stationEntities))
                .collect(Collectors.toList());
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), interStations);
    }

    private InterStation makeInterStation(final InterStationEntity interStationEntity, final List<StationEntity> stationEntities) {
        return new InterStation(interStationEntity.getId(),
                stationEntities.stream()
                        .filter(stationEntity -> stationEntity.getId().equals(interStationEntity.getFrontStationId()))
                        .map(this::makeStation)
                        .findFirst()
                        .orElseThrow(() -> new BusinessException("역이 존재하지 않습니다.")),
                stationEntities.stream()
                        .filter(stationEntity -> stationEntity.getId().equals(interStationEntity.getBackStationId()))
                        .map(this::makeStation)
                        .findFirst()
                        .orElseThrow(() -> new BusinessException("역이 존재하지 않습니다.")),
                interStationEntity.getDistance());
    }

    private Station makeStation(final StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }

    @Override
    public void delete(final Line line) {
        lineDao.deleteById(line.getId());
    }
}
