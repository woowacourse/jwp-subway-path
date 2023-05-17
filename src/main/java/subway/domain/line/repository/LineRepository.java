package subway.domain.line.repository;

import org.springframework.stereotype.Repository;
import subway.domain.line.domain.Line;
import subway.domain.lineDetail.dao.LineDetailDao;
import subway.domain.lineDetail.entity.LineDetailEntity;
import subway.domain.section.dao.SectionDao;
import subway.domain.section.entity.SectionDetailEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final SectionDao sectionDao;
    private final LineDetailDao lineDetailDao;
    private final SectionsMapper sectionsMapper;

    public LineRepository(final SectionDao sectionDao, final LineDetailDao lineDetailDao, final SectionsMapper sectionsMapper) {
        this.sectionDao = sectionDao;
        this.lineDetailDao = lineDetailDao;
        this.sectionsMapper = sectionsMapper;
    }

    public Line findById(Long id) {
        LineDetailEntity lineDetailEntity = lineDetailDao.findById(id);
        List<SectionDetailEntity> sectionsByLineId = sectionDao.findSectionsByLineId(id);
        return new Line(lineDetailEntity, sectionsMapper.sectionsToStations(sectionsByLineId));
    }

    public List<Line> findAll(){
        List<LineDetailEntity> lineDetails = lineDetailDao.findAll();
        return lineDetails.stream()
                .map(LineDetailEntity::getId)
                .map(this::findById)
                .collect(Collectors.toList());
    }
}
