package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.entity.LineEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.exception.DuplicateException;
import subway.exception.ErrorCode;

@Repository
public class LineRepository {
    private final LineDao lineDao;
    private final SectionRepository sectionRepository;

    public LineRepository(LineDao lineDao, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
    }

    public Long save(Line line) {
        duplicateLineName(line.getName());
        return lineDao.save(new LineEntity(line.getName()));
    }

    private void duplicateLineName(String name) {
        if (lineDao.isExisted(name)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NAME);
        }
    }

    public List<Line> findAll() {
        List<LineEntity> lineEntities = lineDao.findAll();
        return lineEntities.stream()
                .map(lineEntity -> findById(lineEntity.getId()))
                .collect(Collectors.toList());
    }

    public Line findById(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId);
        List<Section> sections = sectionRepository.findByLineId(lineId);
        return Line.of(lineEntity.getId(), lineEntity.getName(), sections);
    }
}
