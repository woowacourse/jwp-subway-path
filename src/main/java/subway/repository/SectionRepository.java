package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.line.LineDao;
import subway.dao.section.SectionDao;
import subway.dao.station.StationDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

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

    public Sections findSectionsByLineNumber(final Long lineNumber) {
        final LineEntity lineEntity = lineDao.findByLineNumber(lineNumber);
        return getSections(lineEntity);
    }

    private Sections getSections(final LineEntity lineEntity) {
        final List<SectionEntity> sectionsByLineId = sectionDao.findSectionsByLineId(lineEntity.getLineId());

        final List<Section> sections = sectionsByLineId.stream()
                .map(sectionEntity -> {
                    final StationEntity upStationEntity = stationDao.findById(sectionEntity.getUpStationId());
                    final StationEntity downStationEntity = stationDao.findById(sectionEntity.getDownStationId());
                    final Station upStation = new Station(upStationEntity.getName());
                    final Station downStation = new Station(downStationEntity.getName());

                    return new Section(upStation, downStation, sectionEntity.getDistance());
                })
                .collect(Collectors.toList());

        return new Sections(sections);
    }

    public void updateSectionsByLineNumber(final Sections sections, final Long lineNumber) {
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
        sectionDao.insertBatchSections(sectionEntities);
    }
}
