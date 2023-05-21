package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.domain.line.Line;
import subway.global.exception.line.CanNotFoundLineException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineQueryService {

    private final LineDao lineDao;
    private final SectionQueryService sectionQueryService;

    public LineQueryService(final LineDao lineDao, final SectionQueryService sectionQueryService) {
        this.lineDao = lineDao;
        this.sectionQueryService = sectionQueryService;
    }

    public List<Line> searchLines(final String lineName) {

        if (lineName == null) {
            return searchAllLine();
        }

        return List.of(searchByLineName(lineName));
    }

    public List<Line> searchAllLine() {

        final List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
                           .map(lineEntity -> new Line(
                                   lineEntity.getId(),
                                   lineEntity.getName(),
                                   sectionQueryService.searchSectionsByLineId(lineEntity.getId())))
                           .collect(Collectors.toList());
    }

    public Line searchByLineName(final String lineName) {
        final LineEntity lineEntity =
                lineDao.findByLineName(lineName)
                       .orElseThrow(() -> new CanNotFoundLineException("해당 노선은 존재하지 않습니다."));

        return new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                sectionQueryService.searchSectionsByLineId(lineEntity.getId())
        );
    }

    public boolean isExistLine(final String lineName) {
        return lineDao.findByLineName(lineName).isPresent();
    }
}
