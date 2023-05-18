package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.StationEdgeDao;
import subway.domain.line.Line;
import subway.domain.line.edge.StationEdge;
import subway.entity.LineEntity;
import subway.entity.StationEdgeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Repository
public class DbLineRepository implements LineRepository {

    private final LineDao lineDao;
    private final StationEdgeDao stationEdgeDao;


    public DbLineRepository(final LineDao lineDao, final StationEdgeDao stationEdgeDao) {
        this.lineDao = lineDao;
        this.stationEdgeDao = stationEdgeDao;
    }

    @Override
    public Optional<Line> findById(final Long id) {
        final Optional<LineEntity> optionalLineEntity = lineDao.findById(id);
        if (optionalLineEntity.isEmpty()) {
            return Optional.empty();
        }

        final LineEntity lineEntity = optionalLineEntity.get();

        return convertToDomain(lineEntity);
    }

    private Optional<Line> convertToDomain(final LineEntity lineEntity) {
        final List<StationEdgeEntity> stationEdgeEntities = stationEdgeDao.findByLineId(lineEntity.getId());
        final Set<StationEdge> stationEdges = convertStationEdgeEntityToDomain(stationEdgeEntities);

        return Optional.of(Line.of(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), stationEdges));
    }

    private Set<StationEdge> convertStationEdgeEntityToDomain(final List<StationEdgeEntity> stationEdgeEntities) {
        return stationEdgeEntities.stream()
                .map(entity -> new StationEdge(entity.getUpStationId(), entity.getDownStationId(), entity.getDistance()))
                .collect(Collectors.toSet());
    }

    @Override
    public List<Line> findAll() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        final List<Line> lines = new ArrayList<>();
        final List<StationEdgeEntity> allStationEdgeEntities = stationEdgeDao.findAll();
        for (LineEntity lineEntity : lineEntities) {
            final List<StationEdgeEntity> stationEdgeEntities = allStationEdgeEntities.stream()
                    .filter(stationEdge -> stationEdge.getLineId().equals(lineEntity.getId()))
                    .collect(Collectors.toList());
            final Set<StationEdge> stationEdges = convertStationEdgeEntityToDomain(stationEdgeEntities);
            lines.add(Line.of(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), stationEdges));
        }
        return lines;
    }

    @Override
    public Optional<Line> findByName(final String name) {
        final Optional<LineEntity> optionalLineEntity = lineDao.findByName(name);
        if (optionalLineEntity.isEmpty()) {
            return Optional.empty();
        }
        final LineEntity lineEntity = optionalLineEntity.get();
        return convertToDomain(lineEntity);
    }

    @Override
    public Long create(final Line line) {
        final Long lineEntityId = lineDao.insert(LineEntity.from(line));
        final Set<StationEdge> stationEdges = line.getStationEdges().toSet();

        insertStationEdges(lineEntityId, stationEdges);

        return lineEntityId;
    }

    private void insertStationEdges(final Long lineEntityId, final Set<StationEdge> prevStationEdges) {
        final List<StationEdgeEntity> edgeEntities = prevStationEdges.stream()
                .map(edge -> new StationEdgeEntity(lineEntityId, edge.getUpStationId(), edge.getDownStationId(), edge.getDistance()))
                .collect(Collectors.toList());

        stationEdgeDao.insertAll(edgeEntities);
    }

    @Override
    public void updateStationEdges(final Line line) {
        final Long lineId = line.getId();
        stationEdgeDao.deleteByLineId(lineId);
        insertStationEdges(lineId, line.getStationEdges().toSet());
    }

    @Override
    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
