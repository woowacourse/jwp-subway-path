package subway.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import subway.business.domain.Line;
import subway.business.domain.LineRepository;
import subway.business.domain.Section;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.SectionDao;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;

@Repository
public class DbLineRepository implements LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public DbLineRepository(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    @Override
    public long create(Line line) {
        LineEntity lineEntityToSave = new LineEntity(
                line.getName(),
                line.getUpwardTerminus().getName(),
                line.getDownwardTerminus().getName()
        );
        long lineId = lineDao.insert(lineEntityToSave);

        Section section = line.getSections().get(0);
        SectionEntity sectionEntityToSave = new SectionEntity(
                lineId,
                section.getUpwardStation().getName(),
                section.getDownwardStation().getName(),
                section.getDistance()
        );
        sectionDao.insert(sectionEntityToSave);

        return lineId;
    }

    @Override
    public Line findById(Long id) {
        return null;
    }

    @Override
    public List<Line> findAll() {
        return null;
    }

    @Override
    public void save(Line line) {

    }
}
