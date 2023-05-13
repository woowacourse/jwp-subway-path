package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.v2.LineDaoV2;
import subway.dao.v2.SectionDaoV2;
import subway.dao.v2.StationDaoV2;
import subway.domain.LineDomain;
import subway.domain.StationDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDaoV2 lineDao;
    private final SectionDaoV2 sectionDao;
    private final StationDaoV2 stationDao;

    public LineRepository(final LineDaoV2 lineDao, final SectionDaoV2 sectionDao, final StationDaoV2 stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long saveLine(final String name, final String color) {
        return lineDao.insert(name, color);
    }

    public LineDomain findByLineId(final Long lineId) {
        final LineEntity lineEntity = lineDao.findByLineId(lineId)
                .orElseThrow(() -> new IllegalArgumentException("해당 노선 식별자값은 존재하지 않는 노선의 식별자값입니다."));

        final List<StationDomain> stations = getLineStationsByLineId(lineId);

        return new LineDomain(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                stations
        );
    }

    private List<StationDomain> getLineStationsByLineId(final Long lineId) {
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        final List<Long> distinctStationIds = collectDistinctStationIds(sectionEntities);

        return stationDao.findInStationIds(distinctStationIds)
                .stream()
                .map(stationEntity -> new StationDomain(
                        stationEntity.getId(),
                        stationEntity.getName()
                )).collect(Collectors.toList());
    }

    private List<Long> collectDistinctStationIds(final List<SectionEntity> sectionEntities) {
        final List<Long> stationIds = new ArrayList<>();
        stationIds.addAll(collectUpStationIds(sectionEntities));
        stationIds.addAll(collectDownStationIds(sectionEntities));

        return stationIds.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Long> collectUpStationIds(final List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(SectionEntity::getUpStationId)
                .collect(Collectors.toList());
    }

    private List<Long> collectDownStationIds(final List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(SectionEntity::getDownStationId)
                .collect(Collectors.toList());
    }

    public List<LineDomain> findAll() {
        return lineDao.findAll()
                .stream()
                .map(lineEntity -> new LineDomain(
                        lineEntity.getId(),
                        lineEntity.getName(),
                        lineEntity.getColor(),
                        getLineStationsByLineId(lineEntity.getId())
                )).collect(Collectors.toList());
    }
}
