package subway.repository;

import static java.util.stream.Collectors.groupingBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.StationEdgeDao;
import subway.domain.Line;
import subway.domain.StationEdge;
import subway.domain.StationEdges;
import subway.entity.LineEntity;
import subway.entity.StationEdgeEntity;
import subway.exception.StationNotFoundException;


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
        return lineDao.findById(id).map(this::convertToDomain);
    }

    private Line convertToDomain(LineEntity lineEntity) {
        List<StationEdgeEntity> stationEdgeEntities = stationEdgeDao.findByLineId(lineEntity.getId());
        List<StationEdge> stationEdges = convertToDomain(stationEdgeEntities);

        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), StationEdges.from(stationEdges));
    }

    private List<StationEdge> convertToDomain(List<StationEdgeEntity> stationEdgeEntities) {
        int numberOfStations = stationEdgeEntities.size();

        List<StationEdge> orderedStationEdges = new ArrayList<>();

        StationEdgeEntity firstStationEdgeEntity = getFirstStationEdgeEntity(stationEdgeEntities);
        orderedStationEdges.add(firstStationEdgeEntity.toDomain());
        Long previousId = firstStationEdgeEntity.getId();

        while (orderedStationEdges.size() < numberOfStations) {
            StationEdgeEntity nextStationEdgeEntity = getNextStationEdgeEntity(stationEdgeEntities, previousId);
            orderedStationEdges.add(nextStationEdgeEntity.toDomain());
            previousId = nextStationEdgeEntity.getId();
        }
        return orderedStationEdges;
    }

    private StationEdgeEntity getFirstStationEdgeEntity(List<StationEdgeEntity> stationEdgeEntities) {
        return stationEdgeEntities.stream()
                .filter(stationEdgeEntity -> isFirstStationEdge(stationEdgeEntity))
                .findFirst()
                .orElseThrow(StationNotFoundException::new);
    }

    private boolean isFirstStationEdge(StationEdgeEntity stationEdgeEntity) {
        return stationEdgeEntity.getPreviousStationEdgeId() == null;
    }

    private StationEdgeEntity getNextStationEdgeEntity(List<StationEdgeEntity> stationEdgeEntities,
                                                       Long previousId) {
        return stationEdgeEntities.stream()
                .filter(stationEdge -> !isFirstStationEdge(stationEdge)
                        && stationEdge.getPreviousStationEdgeId().equals(previousId))
                .findFirst().orElseThrow(StationNotFoundException::new);
    }


    @Override
    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        List<StationEdgeEntity> allStationEdgeEntities = stationEdgeDao.findAll();

        Map<Long, List<StationEdgeEntity>> lineIdToStationEdgeEntities = allStationEdgeEntities.stream()
                .collect(groupingBy(StationEdgeEntity::getLineId));

        return lineEntities.stream()
                .map(lineEntity -> batchEntityToDomain(lineIdToStationEdgeEntities, lineEntity))
                .collect(Collectors.toList());
    }

    private Line batchEntityToDomain(Map<Long, List<StationEdgeEntity>> lineIdToStationEdgeEntities,
                                     LineEntity lineEntity) {
        long lineId = lineEntity.getId();
        List<StationEdgeEntity> stationEdgeEntities = lineIdToStationEdgeEntities.get(lineId);
        List<StationEdge> stationEdges = convertToDomain(stationEdgeEntities);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), StationEdges.from(stationEdges));
    }

    @Override
    public Optional<Line> findByName(String name) {
        return lineDao.findByName(name).map(this::convertToDomain);
    }

    @Override
    public Long create(Line line) {
        Long lineEntityId = lineDao.insert(LineEntity.from(line));
        List<StationEdge> stationEdges = line.getStationEdges();
        createStationEdges(lineEntityId, stationEdges);
        return lineEntityId;
    }

    private void createStationEdges(Long lineEntityId, List<StationEdge> stationEdges) {
        Long previousEdgeId = null;
        for (StationEdge stationEdge : stationEdges) {
            previousEdgeId = stationEdgeDao.insert(StationEdgeEntity.of(lineEntityId, stationEdge, previousEdgeId));
        }
    }

    @Override
    public void update(Line line) {
        Long lineId = line.getId();

        List<StationEdge> stationEdges = line.getStationEdges();
        stationEdgeDao.deleteByLineId(lineId);

        createStationEdges(lineId, stationEdges);
    }

    @Override
    public void deleteById(Long id) {
        stationEdgeDao.deleteByLineId(id);
        lineDao.deleteById(id);
    }
}
