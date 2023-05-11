package subway.persistence.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.AdjustPath;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.PathInfo;
import subway.domain.Station;
import subway.persistence.dao.LineStationDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.LineStationEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.SectionEntity.Builder;
import subway.persistence.entity.StationEntity;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineStationDao lineStationDao;

    public SectionRepository(final SectionDao sectionDao, final StationDao stationDao,
            final LineStationDao lineStationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineStationDao = lineStationDao;
    }

    public void insert(final Line line) {
        sectionDao.deleteByLineId(line.getId());

        final List<Station> stations = line.findStationsByOrdered();
        final List<SectionEntity> sectionEntities = new ArrayList<>();

        for (Station sourceStation : stations) {
            final AdjustPath adjustPath = sourceStation.getAdjustPath();
            final List<Station> relationStations = adjustPath.findAllStation();

            for (Station targetStation : relationStations) {
                final PathInfo pathInfo = adjustPath.findPathInfoByStation(targetStation);

                if (pathInfo.isUpStation()) {
                    continue ;
                }

                final SectionEntity sectionEntity = Builder.builder()
                        .lineId(line.getId())
                        .upStationId(sourceStation.getId())
                        .downStationId(targetStation.getId())
                        .distance(pathInfo.getDistance().getDistance())
                        .build();
                sectionEntities.add(sectionEntity);
            }
        }
        sectionDao.insertAll(sectionEntities);
    }

    private boolean isUpEnd(final List<SectionEntity> sectionEntities, final Long stationId) {
        return sectionEntities.stream()
                .filter(sectionEntity -> sectionEntity.containsDownStationId(stationId))
                .count() == 0L;
    }
    
    public void findAllByLine(final Line line) {
        final List<LineStationEntity> stations = lineStationDao.findAllByLineId(line.getId());

        if (stations.isEmpty()) {
            return ;
        }

        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(line.getId());
        final LineStationEntity lineStationEntity = stations.stream()
                .filter(station -> isUpEnd(sectionEntities, station.getStationId()))
                .findAny()
                .orElseThrow();

        Long startStationId = lineStationEntity.getStationId();
        final List<Long> findStationIds = new ArrayList<>();

        findStationIds.add(startStationId);

        for(int i = 0; i < sectionEntities.size(); i++) {
            startStationId = findStationByEnd(sectionEntities, startStationId);
            findStationIds.add(startStationId);
        }

        final List<Station> lineStationsByOrdered = stationDao.findAllByIds(findStationIds)
                .stream()
                .map(StationEntity::to)
                .collect(Collectors.toList());

        final Station upStation = lineStationsByOrdered.get(0);
        final Station downStation = lineStationsByOrdered.get(1);

        line.initialStations(upStation, downStation,
                findDistanceByUpAndDownStationId(sectionEntities, upStation.getId(), downStation.getId()));

        for (int i = 2; i < lineStationsByOrdered.size(); i++) {
            final Station sourceStation = lineStationsByOrdered.get(i - 1);
            final Station targetStation = lineStationsByOrdered.get(i);

            line.addEndStation(sourceStation, targetStation,
                    findDistanceByUpAndDownStationId(sectionEntities, upStation.getId(), downStation.getId()));
        }
    }

    private Long findStationByEnd(final List<SectionEntity> sectionEntities, final Long targetUpStationId) {
        final SectionEntity sectionEntity = sectionEntities.stream()
                .filter(targetSectionEntity -> targetSectionEntity.containsUpStationId(targetUpStationId))
                .findAny().orElseThrow();

        return sectionEntity.getDownStationId();
    }

    private Distance findDistanceByUpAndDownStationId(final List<SectionEntity> sectionEntities,
            final Long upStationId, final Long downStationId) {
        final SectionEntity findSection = sectionEntities.stream()
                .filter(sectionEntity -> sectionEntity.matchesByUpAndDownStationId(upStationId, downStationId))
                .findAny()
                .orElseThrow();

        return Distance.from(findSection.getDistance());
    }
}
