package subway.domain;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.dto.SectionDto;
import subway.dao.entity.LineEntity;
import subway.exception.LineNotFoundException;

@Repository
public class LineRepository {
    private final LineDao lineDao;
    private final SectionDao sectionDao;

    public LineRepository(LineDao lineDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
    }

    public Line findById(Long id) {
        Optional<LineEntity> optionalLineEntity = lineDao.findById(id);
        if (optionalLineEntity.isEmpty()) {
            throw new LineNotFoundException();
        }
        LineEntity lineEntity = optionalLineEntity.get();
        return new Line(id, lineEntity.getName(), lineEntity.getColor(), findSectionsInLine(id));
    }

    private Sections findSectionsInLine(Long lineId) {
        List<SectionDto> foundSections = sectionDao.findAllSectionNamesByLineId(lineId);
        return new Sections(foundSections.stream()
                .map(SectionDto::toDomain)
                .collect(Collectors.toList()));
    }
}
