package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.domain.Line;
import subway.global.exception.line.CanNotDuplicatedLineNameException;
import subway.global.exception.line.CanNotFoundLineException;
import subway.service.dto.LineResponse;
import subway.service.dto.RegisterLineRequest;
import subway.service.dto.SearchAllSectionLineRequest;
import subway.service.dto.SectionInLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionService sectionService;

    public LineService(
            final LineDao lineDao,
            final SectionService sectionService
    ) {

        this.lineDao = lineDao;
        this.sectionService = sectionService;
    }

    public List<LineResponse> searchAllSectionInLines(final SearchAllSectionLineRequest searchAllSectionLineRequest) {

        if (searchAllSectionLineRequest == null) {
            return searchSectionsAllLine();
        }

        return List.of(searchSectionsSpecificLine(searchAllSectionLineRequest));
    }

    private List<LineResponse> searchSectionsAllLine() {

        final List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
                           .map(lineEntity -> {
                               Line line = findByLineName(lineEntity.getName());

                               List<SectionInLineResponse> sectionInLineResponses
                                       = sectionService.mapToSectionInLineResponseFrom(line);

                               return new LineResponse(line.getName(), sectionInLineResponses);
                           })
                           .collect(Collectors.toList());
    }

    private LineResponse searchSectionsSpecificLine(
            final SearchAllSectionLineRequest searchAllSectionLineRequest
    ) {
        final String lineName = searchAllSectionLineRequest.getLineName();
        final Line line = findByLineName(lineName);

        final List<SectionInLineResponse> sectionInLineResponses =
                sectionService.mapToSectionInLineResponseFrom(line);

        return new LineResponse(line.getName(), sectionInLineResponses);
    }

    public void deleteLine(final Long lineId) {
        lineDao.deleteLineById(lineId);
    }

    public Long registerLine(final RegisterLineRequest registerLineRequest) {

        try {
            final String lineName = registerLineRequest.getLineName();
            findByLineName(lineName);
        } catch (CanNotFoundLineException exception) {
            throw new CanNotDuplicatedLineNameException("이미 등록되어 있는 노선입니다. 노선 이름은 중복될 수 없습니다.");
        }

        final Long savedId = lineDao.save(new LineEntity(registerLineRequest.getLineName()));

        sectionService.registerSection(
                registerLineRequest.getCurrentStationName(),
                registerLineRequest.getNextStationName(),
                registerLineRequest.getDistance(),
                savedId
        );

        return savedId;
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
