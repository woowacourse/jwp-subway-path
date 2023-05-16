package subway.persistence.repository;

import org.springframework.stereotype.Repository;
import subway.Entity.EntityMapper;
import subway.Entity.SectionEntity;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class H2SectionRepository implements SectionRepository {

    private final StationDao stationDao;
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public H2SectionRepository(StationDao stationDao, LineDao lineDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public void updateAllSectionsInLine(Line line, List<Section> lineSections) {
        sectionDao.deleteAllByLineId(line.getId());
        sectionDao.insertAll(EntityMapper.convertToSectionEntities(lineSections));
    }

    @Override
    public List<Section> readSectionsByLine(Line line) {
        return mapToSections(sectionDao.selectSectionsByLineId(line.getId()));
    }

    private List<Section> mapToSections(List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(entity -> Section.of(
                        entity.getId(),
                        lineDao.findById(entity.getLineId()),
                        stationDao.findById(entity.getUpwardId()),
                        stationDao.findById(entity.getDownwardId()),
                        entity.getDistance())
                ).collect(Collectors.toList());
    }
}
