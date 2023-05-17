package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.section.Section;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.persistence.entity.SectionEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionRepository(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void insert(final Line line) {
        sectionDao.deleteByLineId(line.getId());

        final List<Section> sections = line.getSections().getSections();
        final List<SectionEntity> entities = sections.stream()
                .map(section -> SectionEntity.of(line.getId(), section))
                .collect(Collectors.toList());

        sectionDao.insertAll(entities);
    }

    public Line findAllSectionByLine(final Line line) {
        final Line newLine = Line.of(line.getId(), line.getName(), line.getColor());
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(line.getId());

        for (SectionEntity sectionEntity : sectionEntities) {
            final Station upStation = stationDao.findById(sectionEntity.getUpStationId()).toDomain();
            final Station downStation = stationDao.findById(sectionEntity.getDownStationId()).toDomain();
            final Distance distance = Distance.from(sectionEntity.getDistance());
            newLine.addSection(Section.of(upStation, downStation, distance));
        }

        return newLine;
    }
}
