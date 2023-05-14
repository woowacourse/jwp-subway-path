package subway.line;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.persistence.LineDao;
import subway.line.persistence.LineEntity;
import subway.section.domain.Sections;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Transactional
    public Long create(final LineCreateDto lineCreateDto) {
        final Optional<LineEntity> lineEntity = lineDao.findByName(lineCreateDto.getName());
        if (lineEntity.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 노선 이름입니다.");
        }
        final Line line = new Line(lineCreateDto.getName(), Sections.empty());
        return lineDao.insert(new LineEntity(line.getLineName()));
    }

    public LineEntity findById(final Long lineId) {
        return lineDao.findById(lineId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선 이름입니다."));
    }

    public List<LineEntity> findAll() {
        return lineDao.findAll();
    }
}
