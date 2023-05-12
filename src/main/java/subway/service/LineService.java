package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.domain.Line;
import subway.service.dto.LineResponse;
import subway.service.dto.RegisterLineRequest;
import subway.service.dto.SearchAllSectionLineRequest;
import subway.service.dto.SectionInLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;
    private final CommonService commonService;
    private final SectionService sectionService;

    public LineService(
            final LineDao lineDao,
            final CommonService commonService,
            final SectionService sectionService
    ) {

        this.lineDao = lineDao;
        this.commonService = commonService;
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
                               Line line = commonService.mapToLineFrom(lineEntity.getName());

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
        final Line line = commonService.mapToLineFrom(lineName);

        final List<SectionInLineResponse> sectionInLineResponses =
                sectionService.mapToSectionInLineResponseFrom(line);

        return new LineResponse(line.getName(), sectionInLineResponses);
    }

    public void deleteLine(final Long lineId) {
        lineDao.deleteLineById(lineId);
    }

    public Long registerLine(final RegisterLineRequest registerLineRequest) {

        final Long savedId = lineDao.save(new LineEntity(registerLineRequest.getLineName()));

        sectionService.registerSection(
                registerLineRequest.getCurrentStationName(),
                registerLineRequest.getNextStationName(),
                registerLineRequest.getDistance(),
                savedId
        );

        return savedId;
    }
}
