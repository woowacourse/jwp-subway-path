package subway.application.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.repository.LineRepository;
import subway.domain.repository.SectionRepository;
import subway.ui.dto.response.StationResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FindStationService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public FindStationService(final LineRepository lineRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<StationResponse> findAllByLine(final Long lineId) {
        List<Section> findSections = sectionRepository.findAllByLineId(lineId);
        if (findSections.isEmpty()) {
            throw new IllegalArgumentException("노선의 역이 없습니다.");
        }

        final Sections sections = new Sections(findSections);

        return StationResponse.of(sections.getSortedStations());
    }

    public List<List<StationResponse>> findAllLine() {
        List<Line> lines = lineRepository.findAll();

        List<List<StationResponse>> allLines = new ArrayList<>();
        for (Line line: lines) {
            final Sections sections = new Sections(sectionRepository.findAllByLineId(line.getId()));
            allLines.add(StationResponse.of(sections.getSortedStations()));
        }
        return allLines;
    }
}
