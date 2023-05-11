package subway.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.StationEdgeDao;
import subway.domain.Line;
import subway.domain.StationEdge;
import subway.entity.LineEntity;
import subway.entity.StationEdgeEntity;
import subway.exception.StationEdgeNotFoundException;


@Repository
public class DbLineRepository implements LineRepository {

    private final LineDao lineDao;
    private final StationEdgeDao stationEdgeDao;


    public DbLineRepository(LineDao lineDao, StationEdgeDao stationEdgeDao) {
        this.lineDao = lineDao;
        this.stationEdgeDao = stationEdgeDao;
    }

    @Override
    public Optional<Line> findById(Long id) {
        Optional<LineEntity> optionalLineEntity = lineDao.findById(id);
        if (optionalLineEntity.isEmpty()) {
            return Optional.empty();
        }

        LineEntity lineEntity = optionalLineEntity.get();

        return convertToDomain(lineEntity);
    }

    private Optional<Line> convertToDomain(LineEntity lineEntity) {
        List<StationEdgeEntity> stationEdgeEntities = stationEdgeDao.findByLineId(lineEntity.getId());
        List<StationEdge> sorted = getStationEdges(stationEdgeEntities);

        return Optional.of(Line.of(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sorted));
    }

    private List<StationEdge> getStationEdges(List<StationEdgeEntity> stationEdgeEntities) {
        int numberOfStations = stationEdgeEntities.size();
        List<StationEdge> sorted = new ArrayList<>();
        StationEdgeEntity first = getFirstStationEdgeEntity(stationEdgeEntities);
        sorted.add(first.toDomain());
        Long previousId = first.getId();

        while (sorted.size() < numberOfStations) {
            StationEdgeEntity next = getNext(stationEdgeEntities, previousId);
            sorted.add(next.toDomain());
            previousId = next.getId();
        }
        return sorted;
    }

    private static StationEdgeEntity getFirstStationEdgeEntity(List<StationEdgeEntity> stationEdgeEntities) {
        StationEdgeEntity first = stationEdgeEntities.stream()
                .filter(stationEdgeEntity -> stationEdgeEntity.getPreviousStationEdgeId() == null)
                .findFirst()
                .get();
        stationEdgeEntities.remove(first);
        return first;
    }

    private static StationEdgeEntity getNext(List<StationEdgeEntity> stationEdgeEntities,
                                             Long previousId) {
        StationEdgeEntity next = stationEdgeEntities.stream()
                .filter(stationEdge -> stationEdge.getPreviousStationEdgeId().equals(previousId))
                .findFirst().get();
        return next;
    }


    @Override
    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        List<Line> lines = new ArrayList<>();
        List<StationEdgeEntity> all = stationEdgeDao.findAll();
        for (LineEntity lineEntity : lineEntities) {
            List<StationEdgeEntity> stationEdgeEntities = all.stream()
                    .filter(stationEdge -> stationEdge.getLineId().equals(lineEntity.getId()))
                    .collect(Collectors.toList());
            List<StationEdge> sorted = getStationEdges(stationEdgeEntities);
            lines.add(Line.of(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), sorted));
        }
        return lines;
    }

    @Override
    public Optional<Line> findByName(String name) {
        Optional<LineEntity> optionalLineEntity = lineDao.findByName(name);
        if (optionalLineEntity.isEmpty()) {
            return Optional.empty();
        }
        LineEntity lineEntity = optionalLineEntity.get();
        return convertToDomain(lineEntity);
    }

    @Override
    public Long create(Line line) {
        Long lineEntityId = lineDao.insert(LineEntity.from(line));
        List<StationEdge> stationEdges = line.getStationEdges();
        Long previousEdgeId = null;
        for (StationEdge stationEdge : stationEdges) {
            previousEdgeId = stationEdgeDao.insert(StationEdgeEntity.of(lineEntityId, stationEdge, previousEdgeId));
        }
        return lineEntityId;
    }

    @Override
    public Long insertStationEdge(Line line, StationEdge stationEdge) {
        Long lineId = line.getId();
        int edgeIndex = line.getStationEdges().indexOf(stationEdge) - 1;

        if (edgeIndex == -1) {
            return stationEdgeDao.insert(StationEdgeEntity.of(lineId, stationEdge, null));
        }
        Long downStationId = line.getStationEdges().get(edgeIndex).getDownStationId();
        StationEdgeEntity previousStationEdge = stationEdgeDao.findByLineIdAndStationId(lineId, downStationId)
                .orElseThrow(StationEdgeNotFoundException::new);

        return stationEdgeDao.insert(StationEdgeEntity.of(lineId, stationEdge, previousStationEdge.getId()));
    }

    @Override
    public void updateStationEdge(Line line, StationEdge stationEdge) {
        Long lineId = line.getId();
        Long downStationId = stationEdge.getDownStationId();
        StationEdgeEntity stationEdgeEntity = stationEdgeDao.findByLineIdAndStationId(lineId, downStationId)
                .orElseThrow(StationEdgeNotFoundException::new);

        StationEdge upStationEdge = line.getStationEdges().get(line.getStationEdges().indexOf(stationEdge) - 1);
        Long previousStationEdgeId = stationEdgeDao.findByLineIdAndStationId(lineId, upStationEdge.getDownStationId())
                .get().getId();
        stationEdgeDao.update(
                StationEdgeEntity.of(stationEdgeEntity.getId(), lineId, stationEdge,
                        previousStationEdgeId));
    }

    @Override
    public void deleteStation(Line line, Long stationId) {
        Long lineId = line.getId();
        stationEdgeDao.deleteByLineIdAndStationId(lineId, stationId);
    }

    @Override
    public void deleteById(Long id) {
        lineDao.deleteById(id);
    }
}
