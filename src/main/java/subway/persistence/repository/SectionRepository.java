package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.application.PathService;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.section.Section;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao, final PathService pathService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void insert(final Line line, final List<Section> orderedSectionPath) {
        sectionDao.deleteByLineId(line.getId());

        final List<SectionEntity> entities = orderedSectionPath.stream()
                .map(section -> SectionEntity.of(line.getId(), section))
                .collect(Collectors.toList());

        sectionDao.insertAll(entities);
    }

    public Line findLineInAllSectionByLineId(final Long id) {
        final Line line = lineDao.findById(id).toDomain();
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(id);

        for (SectionEntity sectionEntity : sectionEntities) {
            final Station upStation = stationDao.findById(sectionEntity.getUpStationId()).toDomain();
            final Station downStation = stationDao.findById(sectionEntity.getDownStationId()).toDomain();
            final Distance distance = Distance.from(sectionEntity.getDistance());
            line.addSection(Section.of(upStation, downStation, distance));
        }

        return line;
    }
}
