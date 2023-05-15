package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.domain.Line;
import subway.global.exception.line.CanNotFoundLineException;
import subway.service.dto.SearchAllSectionLineRequest;

@Service
@Transactional(readOnly = true)
public class LineQueryService {

    private final LineDao lineDao;
    private final SectionQueryService sectionQueryService;

    public LineQueryService(
        final LineDao lineDao,
        final SectionQueryService sectionQueryService
    ) {
        this.lineDao = lineDao;
        this.sectionQueryService = sectionQueryService;
    }

    public List<Line> searchAllSectionInLines(
        final SearchAllSectionLineRequest searchAllSectionLineRequest) {

        if (searchAllSectionLineRequest == null) {
            return searchSectionsAllLine();
        }

        return List.of(searchSectionsSpecificLine(searchAllSectionLineRequest));
    }

    private List<Line> searchSectionsAllLine() {

        final List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
            .map(it -> new Line(
                it.getId(),
                it.getName(),
                sectionQueryService.findSectionsByLineId(it.getId()))
            )
            .collect(Collectors.toList());
    }

    private Line searchSectionsSpecificLine(
        final SearchAllSectionLineRequest searchAllSectionLineRequest
    ) {
        final String lineName = searchAllSectionLineRequest.getLineName();
        return findByLineName(lineName);
    }

    public Line findByLineName(final String lineName) {
        final LineEntity lineEntity =
            lineDao.findLineByName(lineName)
                .orElseThrow(() -> new CanNotFoundLineException("해당 노선은 존재하지 않습니다."));

        return new Line(
            lineEntity.getId(),
            lineEntity.getName(),
            sectionQueryService.findSectionsByLineId(lineEntity.getId())
        );
    }
}
