package subway.application.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.StationResponse;
import subway.application.port.in.line.FindLineUseCase;
import subway.application.port.out.line.LineQueryHandler;
import subway.application.port.out.section.SectionQueryHandler;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineQueryService implements FindLineUseCase {

    private final LineQueryHandler lineQueryHandler;
    private final SectionQueryHandler sectionQueryPort;

    public LineQueryService(final LineQueryHandler lineQueryHandler, final SectionQueryHandler sectionQueryPort) {
        this.lineQueryHandler = lineQueryHandler;
        this.sectionQueryPort = sectionQueryPort;
    }

    public List<StationResponse> findAllByLine(final Long lineId) {
        List<Section> findSections = sectionQueryPort.findAllByLineId(lineId);
        if (findSections.isEmpty()) {
            throw new IllegalArgumentException("노선의 역이 없습니다.");
        }

        final Sections sections = new Sections(findSections);

        return StationResponse.of(sections.getSortedStations());
    }

    public List<List<StationResponse>> findAllLine() {
        List<Line> lines = lineQueryHandler.findAll();

        List<List<StationResponse>> allLines = new ArrayList<>();
        for (Line line: lines) {
            final Sections sections = new Sections(sectionQueryPort.findAllByLineId(line.getId()));
            allLines.add(StationResponse.of(sections.getSortedStations()));
        }
        return allLines;
    }
}
