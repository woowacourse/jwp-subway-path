package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Sections;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.notfound.LineNotFoundException;

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
    public void updateByLineNumber(final Sections sections, final Long lineNumber) {
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
            final LineEntity lineEntity = lineDao.findByLineNumber(lineNumber);
            return getSections(lineEntity);
        }
        throw new LineNotFoundException();
    }

    @Override
    public Sections findByLineId(final Long lineId) {
        final boolean exist = lineDao.isIdExist(lineId);
        if (exist) {
            final LineEntity lineEntity = lineDao.findById(lineId);
            return getSections(lineEntity);
        }
        throw new LineNotFoundException();
    }

    private Sections getSections(final LineEntity lineEntity) {
        final List<SectionEntity> sectionsByLineId = sectionDao.findAllByLineId(lineEntity.getLineId());

        final List<Section> sections = sectionsByLineId.stream()
                .map(sectionEntity -> {
                    final StationEntity upStationEntity = stationDao.findById(sectionEntity.getUpStationId());
                    final StationEntity downStationEntity = stationDao.findById(sectionEntity.getDownStationId());
                    final Station upStation = new Station(upStationEntity.getStationId(), upStationEntity.getName());
                    final Station downStation = new Station(downStationEntity.getStationId(), downStationEntity.getName());

                    return new Section(sectionEntity.getSectionId(), upStation, downStation, sectionEntity.getDistance());
                })
                .collect(Collectors.toList());

        return new Sections(sections, lineEntity.getLineId());
    }
}
