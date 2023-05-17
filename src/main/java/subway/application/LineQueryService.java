package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineQueryService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineQueryService(final LineRepository lineRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<Line> findAllLine() {
        final List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(sectionRepository::findAllSectionByLine)
                .collect(Collectors.toList());
    }

    public Line findLineById(final Long id) {
        final Line line = lineRepository.findById(id);
        return sectionRepository.findAllSectionByLine(line);
    }
}
