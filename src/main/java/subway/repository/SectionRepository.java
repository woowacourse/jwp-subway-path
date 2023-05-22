package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.exception.notfound.LineNotFoundException;
import subway.exception.notfound.SectionNotFoundException;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SectionRepository(final SectionDao sectionDao, final StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public Sections findByLineNumber(final Long lineNumber) {
        final boolean exist = lineDao.isLineNumberExist(lineNumber);
        if (exist) {
            final LineEntity lineEntity = lineDao.findByLineNumber(lineNumber);
            return getSections(lineEntity);
        }
        throw new LineNotFoundException();
    }

    private Sections getSections(final LineEntity lineEntity) {
        final List<SectionEntity> sectionsByLineId = sectionDao.findAllByLineId(lineEntity.getLineId());

        final List<Section> sections = sectionsByLineId.stream()
                .map(sectionEntity -> {
                    final StationEntity upStationEntity = stationDao.findByStationId(sectionEntity.getUpStationId());
                    final StationEntity downStationEntity = stationDao.findByStationId(sectionEntity.getDownStationId());
                    final Station upStation = new Station(upStationEntity.getName());
                    final Station downStation = new Station(downStationEntity.getName());

                    return new Section(upStation, downStation, sectionEntity.getDistance());
                })
                .collect(Collectors.toList());

        return new Sections(sections);
    }

    public void updateByLineNumber(final Sections sections, final Long lineNumber) {
        final boolean exist = lineDao.isLineNumberExist(lineNumber);
        if (!exist) {
            throw new LineNotFoundException();
        }
        final LineEntity lineEntity = lineDao.findByLineNumber(lineNumber);
        final List<SectionEntity> sectionEntities = sections.getSections().stream()
                .map(section -> {
                    final Station upStation = section.getUpStation();
                    final Station downStation = section.getDownStation();
                    final StationEntity upStationEntity = stationDao.findByName(upStation.getName());
                    final StationEntity downStationEntity = stationDao.findByName(downStation.getName());

                    return new SectionEntity(null, lineEntity.getLineId(), upStationEntity.getStationId(), downStationEntity.getStationId(), section.getDistance());
                })
                .collect(Collectors.toList());

        sectionDao.deleteAllByLineId(lineEntity.getLineId());
        sectionDao.batchSave(sectionEntities);
    }
}
