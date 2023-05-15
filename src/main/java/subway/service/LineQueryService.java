package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.domain.Line;
import subway.global.exception.line.CanNotFoundLineException;
import subway.service.dto.SearchAllSectionLineRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineQueryService {

    private final SectionService sectionService;
    private final LineDao lineDao;

    public LineQueryService(final SectionService sectionService, final LineDao lineDao) {
        this.sectionService = sectionService;
        this.lineDao = lineDao;
    }

    public List<Line> searchAllSectionInLines(final SearchAllSectionLineRequest searchAllSectionLineRequest) {

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
                                   sectionService.findSectionsByLineId(it.getId()))
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
                sectionService.findSectionsByLineId(lineEntity.getId())
        );
    }
}
