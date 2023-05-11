package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.service.dto.LineResponse;
import subway.service.dto.SearchAllSectionLineRequest;
import subway.service.dto.SectionInLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;
    private final CommonService commonService;

    public LineService(
            final LineDao lineDao,
            final CommonService commonService
    ) {

        this.lineDao = lineDao;
        this.commonService = commonService;
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
                                       = mapToSectionInLineResponseFrom(line);
                               return new LineResponse(line.getName(), sectionInLineResponses);
                           })
                           .collect(Collectors.toList());
    }

    private List<SectionInLineResponse> mapToSectionInLineResponseFrom(final Line line) {
        return line.getSections()
                   .stream()
                   .map(it -> new SectionInLineResponse(
                           it.getStations().getCurrent().getName(),
                           it.getStations().getNext().getName(),
                           it.getStations().getDistance()))
                   .collect(Collectors.toList());
    }

    private LineResponse searchSectionsSpecificLine(final SearchAllSectionLineRequest searchAllSectionLineRequest) {

        final String lineName = searchAllSectionLineRequest.getLineName();
        final Line line = commonService.mapToLineFrom(lineName);

        final List<SectionInLineResponse> sectionInLineResponses = mapToSectionInLineResponseFrom(line);

        return new LineResponse(line.getName(), sectionInLineResponses);
    }

    public void deleteLine(final Long lineId) {
        lineDao.deleteLineById(lineId);
    }
}
