package subway.persistence.repository.section;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.exception.notfound.LineNotFoundException;
import subway.persistence.dao.line.LineDao;
import subway.persistence.dao.section.SectionDao;
import subway.persistence.dao.station.StationDao;
import subway.persistence.entity.line.LineEntity;
import subway.persistence.entity.section.SectionEntity;
import subway.persistence.entity.station.StationEntity;

@Repository
public class JdbcSectionRepository implements SectionRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public JdbcSectionRepository(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    @Override
    public void save(final Sections sections, final Long lineNumber) {
        final boolean exist = lineDao.isLineNumberExist(lineNumber);
        if (!exist) {
            throw new LineNotFoundException();
        }
        final LineEntity lineEntity = lineDao.findByLineNumber(lineNumber);
        final List<SectionEntity> sectionEntities = sections.getSections().stream()
                .map(section -> new SectionEntity(lineEntity.getLineId(), section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance()))
                .collect(Collectors.toList());

        sectionDao.deleteAllByLineId(lineEntity.getLineId());
        sectionDao.batchSave(sectionEntities);
    }

    @Override
    public Sections findByLineNumber(final Long lineNumber) {
        final boolean exist = lineDao.isLineNumberExist(lineNumber);
        if (exist) {
            final Long lineId = lineDao.findByLineNumber(lineNumber).getLineId();
            final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
            return getSections(lineId, sectionEntities);
        }
        throw new LineNotFoundException();
    }

    @Override
    public Sections findByLineId(final Long lineId) {
        final boolean exist = lineDao.isIdExist(lineId);
        if (exist) {
            final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
            return getSections(lineId, sectionEntities);
        }
        throw new LineNotFoundException();
    }

    @Override
    public List<Sections> findAllByLineIds(final List<Long> lineIds) {
        final Map<Long, List<SectionEntity>> sectionEntitiesByLineId = sectionDao.findAllByLineIds(lineIds).stream()
                .collect(Collectors.groupingBy(SectionEntity::getLineId));

        return sectionEntitiesByLineId.entrySet().stream()
                .map(entry -> {
                    final Long lineId = entry.getKey();
                    final List<SectionEntity> sectionEntities = entry.getValue();
                    return getSections(lineId, sectionEntities);
                })
                .collect(Collectors.toList());
    }

    private Sections getSections(final Long lineId, final List<SectionEntity> sectionEntities) {
        final List<Section> sections = sectionEntities.stream()
                .map(sectionEntity -> {
                    final StationEntity upStationEntity = stationDao.findById(sectionEntity.getUpStationId());
                    final StationEntity downStationEntity = stationDao.findById(sectionEntity.getDownStationId());
                    final Station upStation = new Station(upStationEntity.getStationId(), upStationEntity.getName());
                    final Station downStation = new Station(downStationEntity.getStationId(), downStationEntity.getName());

                    return new Section(sectionEntity.getSectionId(), upStation, downStation, sectionEntity.getDistance());
                })
                .collect(Collectors.toList());

        return new Sections(sections, lineId);
    }
}
