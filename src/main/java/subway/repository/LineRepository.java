package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.dao.v2.LineDaoV2;
import subway.dao.v2.SectionDaoV2;
import subway.dao.v2.StationDaoV2;
import subway.domain.LineDomain;
import subway.domain.SectionDomain;
import subway.domain.SectionsDomain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        final LineEntity lineEntity = getLineEntityOrThrowException(lineId);
        final SectionsDomain sections = collectSectionsByLineId(lineId);

        return lineEntity.toDomain(sections);
    }

    private LineEntity getLineEntityOrThrowException(final Long lineId) {
        return lineDao.findByLineId(lineId)
                .orElseThrow(() -> new IllegalArgumentException("해당 노선 식별자값은 존재하지 않는 노선의 식별자값입니다."));
    }

    private SectionsDomain collectSectionsByLineId(final Long lineId) {
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        final List<StationEntity> upStationEntities = stationDao.findInStationIds(collectUpStationIds(sectionEntities));
        final List<StationEntity> downStationEntities = stationDao.findInStationIds(collectDownStationIds(sectionEntities));

        final List<SectionDomain> sections = IntStream.range(0, sectionEntities.size())
                .mapToObj(index -> sectionEntities.get(index).toDomain(
                        upStationEntities.get(index).toDomain(),
                        downStationEntities.get(index).toDomain()
                )).collect(Collectors.toList());

        return new SectionsDomain(sections);
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
                        collectSectionsByLineId(lineEntity.getId())
                )).collect(Collectors.toList());
    }
}
