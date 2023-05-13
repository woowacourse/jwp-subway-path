package subway.persistence.repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.Direction;
import subway.domain.Distance;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepository(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void insert(final Line line) {
        sectionDao.deleteAllByLineId(line.getId());

        final List<Station> stations = line.findStationsByOrdered();
        final List<SectionEntity> sectionEntities = new ArrayList<>();

        for (Station sourceStation : stations) {
            sectionEntities.addAll(mapToEntities(sourceStation, line.getId()));
        }
        sectionDao.insertAll(sectionEntities);
    }

    private List<SectionEntity> mapToEntities(final Station sourceStation, final Long lineId) {
        final List<Station> adjustPathStations = sourceStation.findAllAdjustPathStation();

        return adjustPathStations.stream()
                .filter(station -> sourceStation.findDirectionByAdjustPathStation(station).matches(Direction.DOWN))
                .map(station -> SectionEntity.Builder.builder()
                        .lineId(lineId)
                        .upStationId(sourceStation.getId())
                        .downStationId(station.getId())
                        .distance(sourceStation.findDistanceByStation(station).getDistance())
                        .build())
                .collect(Collectors.toList());
    }

    public Line findAllByLine(final Line line) {
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(line.getId());

        if (sectionEntities.isEmpty()) {
            return line;
        }

        final Set<Long> stationIds = mapToStationIds(sectionEntities);
        final Map<Long, StationEntity> stations = stationDao.findAllByIds(stationIds)
                .stream()
                .collect(Collectors.toMap(StationEntity::getId, stationEntity -> stationEntity));

        Long terminalStationId = findStartTerminalStationId(sectionEntities, stationIds);

        int count = 0;

        while (count++ < 1) {
            final SectionEntity targetSectionEntity = findEntityByUpStationId(sectionEntities, terminalStationId);
            final Station upStation = stations.get(targetSectionEntity.getUpStationId()).to();
            final Station downStation = stations.get(targetSectionEntity.getDownStationId()).to();

            line.addInitialStations(upStation, downStation, Distance.from(targetSectionEntity.getDistance()));
            terminalStationId = targetSectionEntity.getDownStationId();
        }

        if (count == stationIds.size()) {
            return line;
        }

        while (count++ < stationIds.size()) {
            final SectionEntity targetSectionEntity = findEntityByUpStationId(sectionEntities, terminalStationId);
            final Station upStation = stations.get(targetSectionEntity.getUpStationId()).to();
            final Station downStation = stations.get(targetSectionEntity.getDownStationId()).to();

            line.addEndStation(upStation, downStation, Distance.from(targetSectionEntity.getDistance()));
            terminalStationId = targetSectionEntity.getDownStationId();
        }
        return line;
    }

    private SectionEntity findEntityByUpStationId(final List<SectionEntity> sectionEntities, final Long upStationId) {
        return sectionEntities.stream()
                .filter(sectionEntity -> sectionEntity.matchesUpStationId(upStationId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지정한 역은 해당 노선에 등록되어 있지 않습니다."));
    }

    private boolean isTerminalStation(final List<SectionEntity> sectionEntities, final Long stationId) {
        return sectionEntities.stream()
                .noneMatch(sectionEntity -> sectionEntity.matchesDownStationId(stationId));
    }

    private Long findStartTerminalStationId(final List<SectionEntity> sectionEntities, final Set<Long> stationIds) {
        return stationIds.stream()
                .filter(id -> isTerminalStation(sectionEntities, id))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("노선에 등록된 역이 없습니다."));
    }

    private Set<Long> mapToStationIds(final List<SectionEntity> sectionEntities) {
        Set<Long> stationIds = new HashSet<>();

        for (SectionEntity sectionEntity : sectionEntities) {
            stationIds.add(sectionEntity.getUpStationId());
            stationIds.add(sectionEntity.getDownStationId());
        }
        return stationIds;
    }
}
