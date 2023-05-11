package subway.repository;

import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;
import subway.repository.dao.LineDao;
import subway.repository.dao.SectionDao;

@Repository
public class LineRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public LineRepository(SectionDao sectionDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public LineEntity save(LineEntity LineEntity) {
        LineEntity saveLine = lineDao.insert(LineEntity);
        return saveLine;
    }
}
