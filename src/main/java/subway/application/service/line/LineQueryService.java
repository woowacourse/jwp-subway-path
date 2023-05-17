package subway.application.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.adapter.out.persistence.repository.LineJdbcAdapter;
import subway.adapter.out.persistence.repository.SectionJdbcAdapter;
import subway.application.dto.StationResponse;
import subway.application.port.in.line.FindLineUseCase;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineQueryService implements FindLineUseCase {

    private final LineJdbcAdapter lineJdbcAdapter;
    private final SectionJdbcAdapter sectionJdbcAdapter;

    public LineQueryService(final LineJdbcAdapter lineJdbcAdapter, final SectionJdbcAdapter sectionJdbcAdapter) {
        this.lineJdbcAdapter = lineJdbcAdapter;
        this.sectionJdbcAdapter = sectionJdbcAdapter;
    }

    public List<StationResponse> findAllByLine(final Long lineId) {
        List<Section> findSections = sectionJdbcAdapter.findAllByLineId(lineId);
        if (findSections.isEmpty()) {
            throw new IllegalArgumentException("노선의 역이 없습니다.");
        }

        final Sections sections = new Sections(findSections);

        return StationResponse.of(sections.getSortedStations());
    }

    public List<List<StationResponse>> findAllLine() {
        List<Line> lines = lineJdbcAdapter.findAll();

        List<List<StationResponse>> allLines = new ArrayList<>();
        for (Line line: lines) {
            final Sections sections = new Sections(sectionJdbcAdapter.findAllByLineId(line.getId()));
            allLines.add(StationResponse.of(sections.getSortedStations()));
        }
        return allLines;
    }
}
