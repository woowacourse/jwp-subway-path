package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.StationEdgeDao;
import subway.domain.Line;
import subway.domain.StationEdge;
import subway.entity.LineEntity;
import subway.entity.StationEdgeEntity;
import subway.exception.StationEdgeNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        final List<StationEdge> sorted = convertStationEdgeEntityToDomain(stationEdgeEntities);

        return Optional.of(Line.of(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sorted));
    }

    private List<StationEdge> convertStationEdgeEntityToDomain(final List<StationEdgeEntity> stationEdgeEntities) {
        final int numberOfStations = stationEdgeEntities.size();
        final List<StationEdge> sorted = new ArrayList<>();
        final StationEdgeEntity first = getFirstStationEdgeEntity(stationEdgeEntities);
        sorted.add(first.toDomain());

        Long previousId = first.getId();
        while (sorted.size() < numberOfStations) {
            StationEdgeEntity next = getNext(stationEdgeEntities, previousId);
            sorted.add(next.toDomain());
            previousId = next.getId();
        }
        return sorted;
    }

    private StationEdgeEntity getFirstStationEdgeEntity(final List<StationEdgeEntity> stationEdgeEntities) {
        final StationEdgeEntity first = stationEdgeEntities.stream()
                .filter(stationEdgeEntity -> stationEdgeEntity.getPreviousStationEdgeId() == null)
                .findFirst()
                .orElseThrow(StationEdgeNotFoundException::new);
        stationEdgeEntities.remove(first);
        return first;
    }

    private StationEdgeEntity getNext(final List<StationEdgeEntity> stationEdgeEntities, final Long previousId) {
        return stationEdgeEntities.stream()
                .filter(stationEdge -> stationEdge.getPreviousStationEdgeId().equals(previousId))
                .findFirst()
                .orElseThrow(StationEdgeNotFoundException::new);
    }


    @Override
    public List<Line> findAll() {
        final List<LineEntity> lineEntities = lineDao.findAll();
        final List<Line> lines = new ArrayList<>();
        final List<StationEdgeEntity> all = stationEdgeDao.findAll();
        for (LineEntity lineEntity : lineEntities) {
            final List<StationEdgeEntity> stationEdgeEntities = all.stream()
                    .filter(stationEdge -> stationEdge.getLineId().equals(lineEntity.getId()))
                    .collect(Collectors.toList());
            final List<StationEdge> sorted = convertStationEdgeEntityToDomain(stationEdgeEntities);
            lines.add(Line.of(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sorted));
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
        final List<StationEdge> stationEdges = line.getStationEdges();

        insertStationEdges(lineEntityId, stationEdges);

        return lineEntityId;
    }

    private void insertStationEdges(final Long lineEntityId, final List<StationEdge> stationEdges) {
        Long previousEdgeId = null;
        for (StationEdge stationEdge : stationEdges) {
            previousEdgeId = stationEdgeDao.insert(StationEdgeEntity.of(lineEntityId, stationEdge, previousEdgeId));
        }
    }

    @Override
    public void updateStationEdges(final Line line) {
        final Long lineId = line.getId();
        stationEdgeDao.deleteByLineId(lineId);
        insertStationEdges(lineId, line.getStationEdges());
    }

    @Override
    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }
}
