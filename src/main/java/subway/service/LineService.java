package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.domain.Line;
import subway.service.dto.response.LineResponse;
import subway.service.dto.request.RegisterLineRequest;
import subway.service.dto.response.SectionInLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;
    private final LineMakerService lineMakerService;
    private final SectionService sectionService;

    public LineService(
            final LineDao lineDao,
            final LineMakerService lineMakerService,
            final SectionService sectionService
    ) {

        this.lineDao = lineDao;
        this.lineMakerService = lineMakerService;
        this.sectionService = sectionService;
    }

    public List<LineResponse> searchAllLines() {

        final List<LineEntity> lineEntities = lineDao.findAll();

        return lineEntities.stream()
                           .map(lineEntity -> {
                               Line line = lineMakerService.mapToLineFrom(lineEntity.getName());

                               List<SectionInLineResponse> sectionInLineResponses
                                       = sectionService.mapToSectionInLineResponseFrom(line);

                               return new LineResponse(line.getName(), sectionInLineResponses);
                           })
                           .collect(Collectors.toList());
    }

    public LineResponse searchLine(final long lineId) {
        LineEntity lineEntity = lineMakerService.getLineEntityById(lineId);
        final Line line = lineMakerService.mapToLineFrom(lineEntity.getName());

        final List<SectionInLineResponse> sectionInLineResponses =
                sectionService.mapToSectionInLineResponseFrom(line);

        return new LineResponse(line.getName(), sectionInLineResponses);
    }

    public void deleteLine(final Long lineId) {
        lineDao.deleteLineById(lineId);
    }

    public long registerLine(final RegisterLineRequest registerLineRequest) {

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
